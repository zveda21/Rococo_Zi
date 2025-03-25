package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.UserDataEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDataRepository extends JpaRepository<UserDataEntity, UUID> {

    @Nonnull
    Optional<UserDataEntity> findByUsername(@Nonnull String username);
}
