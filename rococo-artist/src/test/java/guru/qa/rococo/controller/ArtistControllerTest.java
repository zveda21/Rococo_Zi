package guru.qa.rococo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.rococo.model.ArtistJson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Test
    @Sql("/artistsListShouldBeReturned.sql")
    void shouldReturnedArtistsList() throws Exception {
        mockMvc.perform(get("/internal/artist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Vincent van Gogh"))
                .andExpect(jsonPath("$.content[0].biography").value("A Dutch Post-Impressionist painter."))
                .andExpect(jsonPath("$.content[0].photo").value("iVBORw0KGgoAAAANSUhEUgAA..."))
                .andExpect(jsonPath("$.content[1].name").value("Frida Kahlo"))
                .andExpect(jsonPath("$.content[1].biography").value("A Mexican painter known for her self-portraits."))
                .andExpect(jsonPath("$.content[1].photo").value("iVBORw0KGgoAAAANSUhEUgAA..."));
    }

    @Test
    @Sql("/shouldReturnArtistById.sql")
    void shouldReturnArtistById() throws Exception {
        String artistId = "357ed0ec-8018-11ef-86dd-0242ac110002";

        mockMvc.perform(get("/internal/artist/{id}", artistId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Frida Kahlo"))
                .andExpect(jsonPath("$.biography").value("A Mexican painter known for her self-portraits."))
                .andExpect(jsonPath("$.photo").value("iVBORw0KGgoAAAANSUhEUgAA..."));
    }

    @Test
    void shouldNotFoundArtistWithWrongId() throws Exception {
        String wrongArtistId = "e9e1d8f0-570d-11ec-bf63-0242ac130999"; // Invalid UUID

        mockMvc.perform(get("/internal/artist/{id}", wrongArtistId))
                .andExpect(status().isNotFound())  // Expecting 404 Not Found
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.errors[0]").value("Object not found in the database."))
                .andExpect(jsonPath("$.errors[1]").value("The artist not found by id: " + wrongArtistId));
    }

    @Test
    void shouldAddArtist() throws Exception {
        ArtistJson artistJson = new ArtistJson(
                null,
                "Pablo Picasso",
                "A Spanish painter and sculptor, one of the most influential artists of the 20th century.",
                "iVBORw0KGgoAAAANSUhEUgAA..."
        );

        mockMvc.perform(post("/internal/artist")
                        .contentType(APPLICATION_JSON)
                        .content(om.writeValueAsString(artistJson)))
                .andExpect(status().isOk())  // Expecting 200 Created
                .andExpect(jsonPath("$.name").value("Pablo Picasso"))
                .andExpect(jsonPath("$.biography").value("A Spanish painter and sculptor, one of the most influential artists of the 20th century."))
                .andExpect(jsonPath("$.photo").value("iVBORw0KGgoAAAANSUhEUgAA..."));
    }

    @Test
    @Sql("/shouldUpdateArtist.sql")
    void shouldUpdateArtist() throws Exception {
        UUID artistId = UUID.fromString("d1b6bfae-c1b6-11ec-b0a0-0242ac130003");
        ArtistJson updatedArtistJson = new ArtistJson(
                artistId,
                "Claude Monet - Updated",
                "Claude Monet was a famous French painter known for his works of light and atmosphere.",
                "iVBORw0KGgoAAAANSUhEUgAA..."
        );

        mockMvc.perform(patch("/internal/artist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(updatedArtistJson)))
                .andExpect(status().isOk())  // Expecting 200 OK
                .andExpect(jsonPath("$.id").value(String.valueOf(artistId)))
                .andExpect(jsonPath("$.name").value("Claude Monet - Updated"))
                .andExpect(jsonPath("$.biography").value("Claude Monet was a famous French painter known for his works of light and atmosphere."))
                .andExpect(jsonPath("$.photo").value("iVBORw0KGgoAAAANSUhEUgAA..."));
    }
}
