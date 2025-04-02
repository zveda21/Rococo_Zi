package guru.qa.rococo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.rococo.data.Authority;
import guru.qa.rococo.data.AuthorityEntity;
import guru.qa.rococo.data.UserEntity;
import guru.qa.rococo.data.repository.UserRepository;
import guru.qa.rococo.model.UserJson;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static guru.qa.rococo.config.RococoAuthProducerConfiguration.TOPIC_USERS;

@Slf4j
@Component
public class RococoUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public RococoUserService(UserRepository userRepository,
                             PasswordEncoder passwordEncoder, KafkaTemplate<String, String> kafkaTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public @Nonnull String registerUser(@Nonnull String username, @Nonnull String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEnabled(true);
        userEntity.setAccountNonExpired(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(password));

        AuthorityEntity readAuthorityEntity = new AuthorityEntity();
        readAuthorityEntity.setAuthority(Authority.read);
        AuthorityEntity writeAuthorityEntity = new AuthorityEntity();
        writeAuthorityEntity.setAuthority(Authority.write);

        userEntity.addAuthorities(readAuthorityEntity, writeAuthorityEntity);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        log.info("Saved username {} by auth module", savedUserEntity);

        String savedUsername = savedUserEntity.getUsername();

        try {
            UserJson data = UserJson.fromEntity(savedUserEntity);
            String dataAsString = new ObjectMapper().writeValueAsString(data);
            kafkaTemplate.send(TOPIC_USERS, dataAsString);
            log.info("Published message for username {}", savedUsername);
        } catch (Exception e) {
            log.error("Error when publishing message for username {}", savedUsername, e);
        }

        return savedUsername;
    }
}
