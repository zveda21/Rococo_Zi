package guru.qa.rococo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RococoGatewayApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RococoGatewayApplication.class);
        application.run(args);
    }
}
