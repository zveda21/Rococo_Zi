package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.PaintingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaintingRepository extends JpaRepository<PaintingEntity, UUID> {
    Page<PaintingEntity> findByTitleContainingIgnoreCase(Pageable pageable, String title);

    Page<PaintingEntity> findByArtistId(Pageable pageable, UUID id);
}
