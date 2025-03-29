package guru.qa.rococo.service;

import guru.qa.rococo.data.PaintingEntity;
import guru.qa.rococo.data.repository.PaintingRepository;
import guru.qa.rococo.exception.NotFoundException;
import guru.qa.rococo.model.Painting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PaintingService {
    private final PaintingRepository repository;

    public PaintingService(PaintingRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Painting> getAll(String title, Pageable pageable) {
        Page<PaintingEntity> entities = title == null ?
                repository.findAll(pageable) :
                repository.findByTitleContainingIgnoreCase(pageable, title);
        return entities.map(Painting::ofEntity);
    }

    @Transactional(readOnly = true)
    public Painting findByPaintingId(UUID id) {
        Optional<PaintingEntity> optional = repository.findById(id);
        return optional.map(Painting::ofEntity).orElseThrow(() -> new NotFoundException("Painting not found with id " + id));
    }

    @Transactional(readOnly = true)
    public Page<Painting> findByArtistId(UUID id, Pageable pageable) {
        Page<PaintingEntity> entities = repository.findByArtistId(pageable, id);
        return entities.map(Painting::ofEntity);
    }

    @Transactional
    public Painting update(Painting painting) {
        validateFields(painting);
        Optional<PaintingEntity> find = repository.findById(painting.id());
        if (find.isEmpty()) {
            throw new RuntimeException("Painting not found");
        }

        PaintingEntity entity = repository.save(setFields(find.get(), painting));
        return Painting.ofEntity(entity);
    }

    public Painting create(Painting painting) {
        validateFields(painting);

        PaintingEntity entity = repository.save(setFields(new PaintingEntity(), painting));
        return Painting.ofEntity(entity);
    }

    private PaintingEntity setFields(PaintingEntity entity, Painting painting) {
        entity.setTitle(painting.title());
        entity.setDescription(painting.description());

        if (painting.content() != null) {
            entity.setContent(painting.content().getBytes(StandardCharsets.UTF_8));
        }
        
        entity.setArtistId(painting.artistId());
        entity.setMuseumId(painting.museumId());

        return entity;
    }

    private void validateFields(Painting painting) {
        Objects.requireNonNull(painting.title(), "Title should not be null");
        Objects.requireNonNull(painting.description(), "Description should not be null");
        Objects.requireNonNull(painting.artistId(), "Artist Id should not be null");
        Objects.requireNonNull(painting.museumId(), "Museum Id should not be null");
    }
}
