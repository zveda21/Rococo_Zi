package guru.qa.rococo;

import guru.qa.rococo.data.MuseumEntity;
import guru.qa.rococo.data.repository.MuseumRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@SpringBootApplication
public class RococoMuseumApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(RococoMuseumApplication.class);
        springApplication.run(args);
    }

    @Autowired
    MuseumRepository repository;

    @Bean
    public CommandLineRunner updateMuseumPhoto() {
        return args -> {
            var museum = repository.findAll();
            final int images = 3;
            int index = 0;
            for (MuseumEntity entity : museum) {

                try {
                    String file = "images/museum-" + index + ".jpeg";
                    URL resource = RococoMuseumApplication.class.getClassLoader().getResource(file);
                    if (resource == null) {
                        throw new IllegalArgumentException("File not found " + file);
                    }

                    index = (index + 1) % images;

                    byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
                    String base64 = Base64.encodeBase64String(bytes);
                    String content = "data:image/jpeg;base64," + base64;

                    entity.setPhoto(content.getBytes(StandardCharsets.UTF_8));
                    repository.save(entity);

                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
