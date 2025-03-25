package guru.qa.rococo.controller.client;

import guru.qa.rococo.data.Artist;
import guru.qa.rococo.exception.NoResponseException;
import guru.qa.rococo.model.page.RestPage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Component
public class ArtistClient {
    private final String url;
    private final RestTemplate restTemplate;

    public ArtistClient(RestTemplate restTemplate, @Value("${rococo-artist.base-uri}") String artistUri) {
        this.restTemplate = restTemplate;
        this.url = artistUri + "/internal/artist";
    }

    @SuppressWarnings("unchecked")
    public List<Artist> getAll() {
        try {
            return Optional.ofNullable(restTemplate.getForEntity(url, RestPage.class).getBody()).map(RestPage::getContent).orElse(List.of());
        } catch (Exception e) {
            throw new NoResponseException("No REST response in " + url, e);
        }
    }

    public Artist getById(UUID id) {
        final String url = this.url + "/" + id.toString();
        try {
            return restTemplate.getForEntity(url, Artist.class).getBody();
        } catch (Exception e) {
            throw new NoResponseException("No REST response in " + url, e);
        }
    }
}
