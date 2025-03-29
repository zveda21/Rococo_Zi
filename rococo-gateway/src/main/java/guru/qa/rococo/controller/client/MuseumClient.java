package guru.qa.rococo.controller.client;

import guru.qa.rococo.exception.NoResponseException;
import guru.qa.rococo.model.Museum;
import guru.qa.rococo.model.page.RestPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class MuseumClient {
    private final String url;
    private final RestTemplate restTemplate;

    public MuseumClient(@Value("${rococo-museum.base-uri}") String url, RestTemplate restTemplate) {
        this.url = url + "/internal/museum";
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public List<Museum> getAll(String title) {
        final String url = this.url + (title != null ? "?title=" + title : "");
        try {
            return Optional.ofNullable(restTemplate.getForEntity(url, RestPage.class).getBody()).map(RestPage::getContent).orElse(List.of());
        } catch (Exception e) {
            log.error("Error {}", e.getMessage(), e);
            throw new NoResponseException("No REST response in " + url, e);
        }
    }

    public Museum getById(UUID id) {
        final String url = this.url + "/" + id.toString();
        try {
            return restTemplate.getForEntity(url, Museum.class).getBody();
        } catch (Exception e) {
            log.error("Error {}", e.getMessage(), e);
            throw new NoResponseException("No REST response in " + url, e);
        }
    }
}
