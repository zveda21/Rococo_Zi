package guru.qa.rococo;

import guru.qa.rococo.data.PaintingEntity;
import guru.qa.rococo.data.repository.PaintingRepository;
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
public class RococoPaintingApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RococoPaintingApplication.class);
        application.run(args);
    }

    @Autowired
    private PaintingRepository repository;
    @Bean
    public CommandLineRunner updateImages() {
        return args -> {
            final int images = 3;
            int index = 0;
            var allPaintings = repository.findAll();
            for (PaintingEntity painting : allPaintings) {
                try {
                    String file = "images/art-" + index + ".jpeg";
                    URL resource = RococoPaintingApplication.class.getClassLoader().getResource(file);
                    if (resource == null) {
                        throw new IllegalArgumentException("File not found " + file);
                    }

                    index = (index + 1) % images;

                    byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
                    String base64 = Base64.encodeBase64String(bytes);
                    String content = "data:image/jpeg;base64," + base64;

                    painting.setContent(content.getBytes(StandardCharsets.UTF_8));
                    repository.save(painting);
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}