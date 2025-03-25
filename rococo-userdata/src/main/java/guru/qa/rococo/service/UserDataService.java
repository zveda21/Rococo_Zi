package guru.qa.rococo.service;

import guru.qa.rococo.data.UserDataEntity;
import guru.qa.rococo.data.repository.UserDataRepository;
import guru.qa.rococo.ex.NotFoundException;
import guru.qa.rococo.model.UserDataJson;
import jakarta.annotation.Nonnull;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Component
public class UserDataService {

    private static final Logger LOG = LoggerFactory.getLogger(UserDataService.class);

    private final UserDataRepository userDataRepository;

    @Autowired
    public UserDataService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Transactional
    @KafkaListener(topics = "users", groupId = "userdata")
    public void listener(@Payload UserDataJson user, ConsumerRecord<String, UserDataJson> cr) {
        userDataRepository.findByUsername(user.username())
                .ifPresentOrElse(
                        u -> LOG.info("### User already exist in DB, kafka event will be skipped: {}", cr.toString()),
                        () -> {
                            LOG.info("### Kafka consumer record: {}", cr.toString());

                            UserDataEntity userDataEntity = new UserDataEntity();
                            userDataEntity.setUsername(user.username());
                            UserDataEntity userEntity = userDataRepository.save(userDataEntity);

                            LOG.info(
                                    "### User '{}' successfully saved to database with id: {}",
                                    user.username(),
                                    userEntity.getId()
                            );
                        }
                );
    }

    @Transactional
    public @Nonnull
    UserDataJson update(@Nonnull UserDataJson user) {
        UserDataEntity userEntity = userDataRepository.findByUsername(user.username())
                .orElseGet(() -> {
                    UserDataEntity emptyUser = new UserDataEntity();
                    emptyUser.setUsername(user.username());
                    return emptyUser;
                });

        userEntity.setFirstname(user.firstname());
        userEntity.setLastname(user.lastname());
        userEntity.setImage(user.image() != null ? user.image().getBytes(StandardCharsets.UTF_8) : null);

        UserDataEntity saved = userDataRepository.save(userEntity);
        return UserDataJson.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public @Nonnull
    UserDataJson getCurrentUser(@Nonnull String username) {
        return userDataRepository.findByUsername(username).map(UserDataJson::fromEntity)
                .orElseThrow(
                        () -> new NotFoundException("Can`t find user by username: '" + username + "'")
                );
    }
}
