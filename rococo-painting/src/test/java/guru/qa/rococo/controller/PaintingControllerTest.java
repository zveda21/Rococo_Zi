package guru.qa.rococo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class PaintingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Test
    @Sql("/insertPaintings.sql")
    void shouldReturnedPaintingsList() throws Exception {
        mockMvc.perform(get("/internal/painting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("Starry Night"))
                .andExpect(jsonPath("$.content[0].description").value("A painting by Vincent van Gogh."))
                .andExpect(jsonPath("$.content[0].content").value("iVBORw0KGgoAAAANSUhEUgAA..."))
                .andExpect(jsonPath("$.content[1].title").value("The Two Fridas"))
                .andExpect(jsonPath("$.content[1].description").value("A painting by Frida Kahlo."))
                .andExpect(jsonPath("$.content[1].content").value("iVBORw0KGgoAAAANSUhEUgAA..."));
    }

    @Test
    @Sql("/insertPaintings.sql")
    void shouldReturnPaintingById() throws Exception {
        String paintingId = "3c9abc0f-0858-4f17-bb19-9c1e359f1f02";

        mockMvc.perform(get("/internal/painting/{id}", paintingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Two Fridas"))
                .andExpect(jsonPath("$.description").value("A painting by Frida Kahlo."))
                .andExpect(jsonPath("$.content").value("iVBORw0KGgoAAAANSUhEUgAA..."));
    }

    @Test
    void shouldReturnNotFoundPaintingById() throws Exception {
        String paintingId = UUID.randomUUID().toString();

        mockMvc.perform(get("/internal/painting/{id}", paintingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.errors[0]").value("Object not found in the database."))
                .andExpect(jsonPath("$.errors[1]").value("Painting not found with id " + paintingId));
    }

    @Test
    void shouldAddNewPainting() throws Exception {
        final String artistId = "ea892478-f670-4473-8a11-d9a1dfa84983";
        final String museumId = "b03fd3f9-4abe-4a3c-85f9-cf006a94a0ce";

        String newPaintingJson = """
                {
                    "title": "The Persistence of Memory",
                    "description": "A painting by Salvador Dalí.",
                    "content": "iVBORw0KGgoAAAANSUhEUgAA...",
                    "artistId": "ea892478-f670-4473-8a11-d9a1dfa84983",
                    "museumId": "b03fd3f9-4abe-4a3c-85f9-cf006a94a0ce"
                }
                """;

        mockMvc.perform(post("/internal/painting")
                        .contentType(APPLICATION_JSON)
                        .content(newPaintingJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Persistence of Memory"))
                .andExpect(jsonPath("$.description").value("A painting by Salvador Dalí."))
                .andExpect(jsonPath("$.content").value("iVBORw0KGgoAAAANSUhEUgAA..."))
                .andExpect(jsonPath("$.artistId").value(artistId))
                .andExpect(jsonPath("$.museumId").value(museumId));
    }

    @Test
    @Sql("/insertPaintings.sql")
    void shouldUpdatePainting() throws Exception {
        String paintingId = "3c9abc0f-0858-4f17-bb19-9c1e359f1f02";

        String updatedPaintingJson = """
                {
                    "id": "3c9abc0f-0858-4f17-bb19-9c1e359f1f02",
                    "title": "The Two Fridas - Updated",
                    "description": "An updated description.",
                    "content": "iVBORw0KGgoAAAANSUhEUgAA...",
                    "artistId": "ea892478-f670-4473-8a11-d9a1dfa84983",
                    "museumId": "b03fd3f9-4abe-4a3c-85f9-cf006a94a0ce"
                }
                """;

        mockMvc.perform(patch("/internal/painting")
                        .contentType(APPLICATION_JSON)
                        .content(updatedPaintingJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paintingId))
                .andExpect(jsonPath("$.title").value("The Two Fridas - Updated"))
                .andExpect(jsonPath("$.description").value("An updated description."))
                .andExpect(jsonPath("$.content").value("iVBORw0KGgoAAAANSUhEUgAA..."))
                .andExpect(jsonPath("$.artistId").value("ea892478-f670-4473-8a11-d9a1dfa84983"))
                .andExpect(jsonPath("$.museumId").value("b03fd3f9-4abe-4a3c-85f9-cf006a94a0ce"));
    }
}
