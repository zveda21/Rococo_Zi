package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.UserEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Nullable
    Optional<UserEntity> findByUsername(@Nonnull String username);
}
