package guru.qa.rococo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Configuration
public class RococoGatewayServiceConfig {

    public static final int ONE_MB = 1024 * 1024;
    public static final String OPEN_API_AUTH_SCHEME = "bearer";

    private final String rococoGatewayBaseUri;

    public RococoGatewayServiceConfig(@Value("${rococo-gateway.base-uri}") String rococoGatewayBaseUri) {
        this.rococoGatewayBaseUri = rococoGatewayBaseUri;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public OpenAPI openAPI() {
        Server server = new Server();
        server.setUrl(rococoGatewayBaseUri);
        return new OpenAPI()
                .servers(List.of(server))
                .info(new Info()
                        .title("Rococo Gateway API Documentation")
                        .version("1.0")
                        .description("API documentation with Swagger and SpringDoc"))
                .addSecurityItem(new SecurityRequirement().addList(OPEN_API_AUTH_SCHEME))
                .components(new Components()
                        .addSecuritySchemes(OPEN_API_AUTH_SCHEME, new SecurityScheme()
                                .name(OPEN_API_AUTH_SCHEME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
