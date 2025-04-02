package guru.qa.rococo.service;

import guru.qa.rococo.data.GeoEntity;
import guru.qa.rococo.data.MuseumEntity;
import guru.qa.rococo.data.repository.GeoRepository;
import guru.qa.rococo.data.repository.MuseumRepository;
import guru.qa.rococo.exception.NotFoundException;
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
        return entity.map(Museum::ofEntity).orElseThrow(() -> new NotFoundException("Museum not found " + id));
    }

    @Transactional
    public Museum update(Museum museum) {
        Optional<MuseumEntity> find = museumRepository.findById(museum.id());
        if (find.isEmpty()) {
            throw new NotFoundException("Museum not found " + museum.id());
        }

        MuseumEntity entity = find.get();
        setFields(entity, museum);

        entity = museumRepository.save(entity);
        log.info("Museum {} updated", entity.getId());

        return Museum.ofEntity(entity);
    }

    @Transactional
    public Museum create(Museum museum) {
        MuseumEntity entity = new MuseumEntity();
        setFields(entity, museum);

        entity = museumRepository.save(entity);
        return Museum.ofEntity(entity);
    }

    private void setFields(MuseumEntity entity, Museum museum) {
        final var city = museum.geo().city();
        Optional<GeoEntity> findGeo = geoRepository.findByCityIgnoreCase(city);
        if (findGeo.isEmpty()) {
            throw new NotFoundException("Location with city name " + city + " not found");
        }

        entity.setTitle(museum.title());
        entity.setDescription(museum.description());
        entity.setGeo(findGeo.get());

        if (museum.photo() != null) {
            entity.setPhoto(museum.photo().getBytes(StandardCharsets.UTF_8));
        }
    }
}
