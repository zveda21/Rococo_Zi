package guru.qa.rococo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.rococo.model.UserDataJson;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserdataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Test
    @Sql(scripts = "/currentUserShouldBeReturned.sql")
    void currentUserShouldBeReturned() throws Exception {
        mockMvc.perform(get("/internal/user/current")
                        .contentType(APPLICATION_JSON)
                        .param("username", "zitest")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("zitest"))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"))
                .andExpect(jsonPath("$.image").isNotEmpty());
    }

    @Test
    @Sql("/updateUserData.sql")
    void checkUpdateUserData() throws Exception {
        UserDataJson updatedUser = new UserDataJson(
                UUID.fromString("357ed0ec-8018-11ef-86dd-0242ac110034"),
                "zitest",
                "Zveda",
                "Doe",
                "iVBORw0KGgoAAAANSUhEUgAA..."
        );

        mockMvc.perform(post("/internal/user/update")
                        .contentType(APPLICATION_JSON)
                        .content(om.writeValueAsString(updatedUser))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("zitest"))
                .andExpect(jsonPath("$.firstname").value("Zveda"))
                .andExpect(jsonPath("$.lastname").value("Doe"))
                .andExpect(jsonPath("$.image").isNotEmpty())
                .andExpect(jsonPath("$.image").value("iVBORw0KGgoAAAANSUhEUgAA..."));
    }
}
