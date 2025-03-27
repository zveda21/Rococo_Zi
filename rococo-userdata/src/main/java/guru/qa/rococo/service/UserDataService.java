package guru.qa.rococo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.rococo.data.UserDataEntity;
import guru.qa.rococo.data.repository.UserDataRepository;
import guru.qa.rococo.ex.NotFoundException;
import guru.qa.rococo.model.UserDataJson;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class UserDataService {

    private final UserDataRepository userDataRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserDataService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
        objectMapper = new ObjectMapper();
    }

    @KafkaListener(topics = "users", groupId = "rococo-userdata")
    public void listener(ConsumerRecord<String, String> record) {
        log.info("Message received {}", record.value());
        try {
            UserDataJson userDataJson = objectMapper.readValue(record.value(), UserDataJson.class);
            final String username = userDataJson.username();

            userDataRepository.findByUsername(username)
                    .ifPresentOrElse(
                            u -> log.info("User already exist in DB, kafka event will be skipped: {}", record.value()),
                            () -> {
                                UserDataEntity userDataEntity = new UserDataEntity();
                                userDataEntity.setUsername(username);
                                UserDataEntity userEntity = userDataRepository.save(userDataEntity);

                                log.info(
                                        "User '{}' successfully saved to database with id: {}",
                                        username,
                                        userEntity.getId()
                                );
                            }
                    );
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize message {}. Message will be skipped.", record.value());
        }
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
    public @Nonnull UserDataJson getCurrentUser(@Nonnull String username) {
        return userDataRepository.findByUsername(username).map(UserDataJson::fromEntity)
                .orElseThrow(
                        () -> new NotFoundException("Can`t find user by username: '" + username + "'")
                );
    }
}
