package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.GeoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GeoRepository extends JpaRepository<GeoEntity, UUID> {
     Optional<GeoEntity> findByCityIgnoreCase(String city);
}
