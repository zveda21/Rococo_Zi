package guru.qa.rococo;

import guru.qa.rococo.data.UserEntity;
import guru.qa.rococo.data.repository.UserRepository;
import guru.qa.rococo.service.PropertiesLogger;
import guru.qa.rococo.service.RococoUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@Slf4j
@SpringBootApplication
public class RococoAuthApplication {

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(RococoAuthApplication.class);
        springApplication.addListeners(new PropertiesLogger());
        springApplication.run(args);
    }

    @Autowired
    private RococoUserService userService;
    @Autowired
    private UserRepository userRepository;

    @Bean
    CommandLineRunner defaultUserCreator() {
        return args -> {
            Optional<UserEntity> user = userRepository.findByUsername(DEFAULT_USERNAME);
            if (user.isPresent()) {
                return;
            }

            try {
                userService.registerUser(DEFAULT_PASSWORD, DEFAULT_PASSWORD);
                log.info("Default user created");
            } catch (Exception e) {
                log.error("Error occurred when creating default user.");
            }
        };
    }
}