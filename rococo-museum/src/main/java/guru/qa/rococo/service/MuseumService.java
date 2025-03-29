package guru.qa.rococo.service;

import guru.qa.rococo.data.GeoEntity;
import guru.qa.rococo.data.MuseumEntity;
import guru.qa.rococo.data.repository.GeoRepository;
import guru.qa.rococo.data.repository.MuseumRepository;
import guru.qa.rococo.exception.NotFondException;
import guru.qa.rococo.model.Museum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class MuseumService {
    private final MuseumRepository museumRepository;
    private final GeoRepository geoRepository;

    @Autowired
    public MuseumService(MuseumRepository museumRepository, GeoRepository geoRepository) {
        this.museumRepository = museumRepository;
        this.geoRepository = geoRepository;
    }

    @Transactional(readOnly = true)
    public Page<Museum> getAll(String title, Pageable pageable) {
        Page<MuseumEntity> entities = title == null ?
                museumRepository.findAll(pageable) :
                museumRepository.findByTitleContainingIgnoreCase(pageable, title);
        return entities.map(Museum::ofEntity);
    }

    @Transactional(readOnly = true)
    public Museum findByMuseumId(UUID id) {
        Optional<MuseumEntity> entity = museumRepository.findById(id);
        return entity.map(Museum::ofEntity).orElseThrow(NotFondException::new);
    }

    public Museum update(Museum museum) {
        Optional<MuseumEntity> find = museumRepository.findById(museum.id());
        if (find.isEmpty()) {
            throw new NotFondException("Museum " + museum.id() + " not found");
        }

        // todo validation
        final var city = museum.geo().city();
        Optional<GeoEntity> findGeo = geoRepository.findByCityIgnoreCase(city);
        if (findGeo.isEmpty()) {
            throw new NotFondException("Location with city name " + city + " not found");
        }

        MuseumEntity entity = find.get();
        entity.setTitle(museum.title());
        entity.setDescription(museum.description());
        entity.setGeo(findGeo.get());
        if (museum.photo() != null) {
            entity.setPhoto(museum.photo().getBytes(StandardCharsets.UTF_8));
        }

        entity = museumRepository.save(entity);
        log.info("Museum {} updated", entity.getId());

        return Museum.ofEntity(entity);
    }
}
