package guru.qa.rococo.controller.client;

import guru.qa.rococo.model.Museum;
import guru.qa.rococo.model.page.RestPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
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

    public List<Museum> getAll(Pageable pageable, String title) {
        Objects.requireNonNull(pageable, "Pageable cannot be null");
        String url = this.url + "?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();
        if (title != null && !title.isBlank()) {
            url += "&title=" + title;
        }
        List<?> pageData = Optional.ofNullable(restTemplate.getForEntity(url, RestPage.class).getBody()).map(RestPage::getContent).orElse(List.of());
        return ClientUtils.convertPageToTypedList(pageData, Museum.class);
    }

    public Museum getById(UUID id) {
        final String url = this.url + "/" + id.toString();
        return restTemplate.getForEntity(url, Museum.class).getBody();

    }

    public Museum update(Museum museum) {
        HttpEntity<Museum> request = new HttpEntity<>(museum);
        return restTemplate.patchForObject(url, request, Museum.class);
    }

    public Museum create(Museum museum) {
        HttpEntity<Museum> request = new HttpEntity<>(museum);
        return restTemplate.postForObject(url, request, Museum.class);
    }
}
