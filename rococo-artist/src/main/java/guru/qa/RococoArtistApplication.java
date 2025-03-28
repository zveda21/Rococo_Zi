package guru.qa;

import guru.qa.rococo.data.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;

@SpringBootApplication
public class RococoArtistApplication {
    @Autowired
    private ArtistRepository artistRepository;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(RococoArtistApplication.class);
        springApplication.run(args);

    }

    @Bean
    public CommandLineRunner checkArtists() {
        return args -> {
            // Fetch all artists from the database (with pagination)
            var artists = artistRepository.findAll(PageRequest.of(0, 10)); // Adjust page and size as needed
            System.out.println("Artists in the database:");
            artists.forEach(artist -> {
                System.out.println("ID: " + artist.getId() + ", Name: " + artist.getName());
            });
        };
    }
}