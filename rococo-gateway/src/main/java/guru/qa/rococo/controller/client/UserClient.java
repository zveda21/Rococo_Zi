package guru.qa.rococo.controller.client;

import guru.qa.rococo.exception.NoResponseException;
import guru.qa.rococo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class UserClient {
    private final String url;
    private final RestTemplate restTemplate;

    public UserClient(RestTemplate restTemplate, @Value("${rococo-userdata.base-uri}") String userDataUrl) {
        this.url = userDataUrl + "/internal/user";
        this.restTemplate = restTemplate;
    }

    public User getByUsername(String username) {
        final String url = this.url + "/current?username=" + username;
        try {
            return restTemplate.getForEntity(url, User.class).getBody();
        } catch (Exception e) {
            log.error("Error {}", e.getMessage(), e);
            throw new NoResponseException("No REST response in " + url, e);
        }
    }

    public User update(User user) {
        final String url = this.url + "/update";
        try {

            // todo user.avatar --> user.image

            HttpEntity<User> request = new HttpEntity<>(user);
            ResponseEntity<User> response = restTemplate.postForEntity(url, request, User.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new NoResponseException("No REST response in " + url);
            }
        } catch (Exception e) {
            throw new NoResponseException("No REST response in " + url, e);
        }
    }

}
