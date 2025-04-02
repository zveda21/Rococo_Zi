package guru.qa.rococo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.rococo.model.GeoLocation;
import guru.qa.rococo.model.Museum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MuseumControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Test
    @Sql("/insertMuseumData.sql")
    void shouldReturnedMuseumList() throws Exception {
        mockMvc.perform(get("/internal/museum"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].id").value("4800753b-fd5a-46ce-b66e-03c64aa70990"))
                .andExpect(jsonPath("$.content[0].title").value("British Museum"))
                .andExpect(jsonPath("$.content[0].description").exists())
                .andExpect(jsonPath("$.content[0].geo").exists())
                .andExpect(jsonPath("$.content[1].id").value("e6d450a1-7d5d-4ba7-a8d5-60b263d38dca"))
                .andExpect(jsonPath("$.content[1].title").value("Louvre Museum"))
                .andExpect(jsonPath("$.content[1].description").exists())
                .andExpect(jsonPath("$.content[1].geo").exists());
    }

    @Test
    @Sql("/insertMuseumData.sql")
    void shouldReturnedMuseumListWithTitle() throws Exception {
        mockMvc.perform(get("/internal/museum?title=british"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value("4800753b-fd5a-46ce-b66e-03c64aa70990"))
                .andExpect(jsonPath("$.content[0].title").value("British Museum"))
                .andExpect(jsonPath("$.content[0].description").exists())
                .andExpect(jsonPath("$.content[0].geo").exists());
    }

    @Test
    @Sql("/insertMuseumData.sql")
    void shouldReturnMuseumById() throws Exception {
        final String museumId = "e6d450a1-7d5d-4ba7-a8d5-60b263d38dca";

        mockMvc.perform(get("/internal/museum/{id}", museumId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(museumId))
                .andExpect(jsonPath("$.title").value("Louvre Museum"))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.geo").exists());
    }

    @Test
    @Sql("/insertMuseumData.sql")
    void shouldReturnNotFoundMuseumById() throws Exception {
        final String nonExistentMuseumId = UUID.randomUUID().toString();

        mockMvc.perform(get("/internal/museum/{id}", nonExistentMuseumId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql("/insertMuseumData.sql")
    void shouldUpdateMuseum() throws Exception {
        final String museumId = "e6d450a1-7d5d-4ba7-a8d5-60b263d38dca";
        final String updatedTitle = "Updated Museum Title";
        final String updatedDescription = "Updated Museum Description";

        String requestBody = om.writeValueAsString(
                new Museum(
                        UUID.fromString(museumId),
                        updatedTitle,
                        updatedDescription,
                        null,
                        new GeoLocation("Paris", null)));

        mockMvc.perform(patch("/internal/museum")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(museumId))
                .andExpect(jsonPath("$.title").value(updatedTitle))
                .andExpect(jsonPath("$.description").value(updatedDescription))
                .andExpect(jsonPath("$.geo.city").value("Paris"))
                .andExpect(jsonPath("$.geo.country.name").value("France"));
    }
}
