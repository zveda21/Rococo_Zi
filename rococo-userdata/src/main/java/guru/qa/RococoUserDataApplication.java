package guru.qa;

import guru.qa.rococo.data.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;

@SpringBootApplication
public class RococoUserDataApplication {
    @Autowired
    private UserDataRepository userDataRepository;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(RococoUserDataApplication.class);
        springApplication.run(args);

    }


    @Bean
    public CommandLineRunner checkArtists() {
        return args -> {
            // Fetch all artists from the database (with pagination)
            var artists = userDataRepository.findAll(PageRequest.of(0, 10)); // Adjust page and size as needed
            System.out.println("Artists in the database:");
            artists.forEach(artist -> {
                System.out.println("ID: " + artist.getId() + ", Name: " + artist.getFirstname());
            });
        };
    }
}