package guru.qa.rococo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class RococoMuseumApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(RococoMuseumApplication.class);
        springApplication.run(args);
    }
}
