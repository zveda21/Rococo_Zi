package guru.qa.rococo.controller.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import guru.qa.rococo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
        return restTemplate.getForEntity(url, User.class).getBody();
    }

    public User update(User user) {
        final String url = this.url + "/update";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode requestData = objectMapper.createObjectNode();
            requestData.put("id", user.id().toString());
            requestData.put("username", user.username());
            requestData.put("firstname", user.firstname());
            requestData.put("lastname", user.lastname());
            requestData.put("image", user.avatar());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(
                    objectMapper.writeValueAsString(requestData),
                    headers);

            ResponseEntity<User> response = restTemplate.postForEntity(url, request, User.class);
            return response.getBody();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
