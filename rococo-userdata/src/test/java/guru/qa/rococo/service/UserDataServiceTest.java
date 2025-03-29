package guru.qa.rococo.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import guru.qa.rococo.data.repository.UserDataRepository;
import guru.qa.rococo.ex.NotFoundException;
import guru.qa.rococo.model.UserDataJson;
import guru.qa.rococo.data.UserDataEntity;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserDataServiceTest {

    @Mock
    private UserDataRepository userDataRepository;

    @InjectMocks
    private UserDataService userDataService;


    @Test
    void testUpdateUserInfo() {
        UserDataJson inputUser = new UserDataJson(UUID.randomUUID(), "example_user12", "UpdatedFirstName", "UpdatedLastName", "updated_image.png");
        UserDataEntity existingUser = new UserDataEntity();
        existingUser.setUsername("example_user12");
        existingUser.setFirstname("OldFirstName");
        existingUser.setLastname("OldLastName");

        when(userDataRepository.findByUsername("example_user12")).thenReturn(Optional.of(existingUser));
        when(userDataRepository.save(any(UserDataEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDataJson updatedUser = userDataService.update(inputUser);

        assertEquals("example_user12", updatedUser.username());
        assertEquals("UpdatedFirstName", updatedUser.firstname());
        assertEquals("UpdatedLastName", updatedUser.lastname());
        assertEquals("updated_image.png", updatedUser.image());
    }

    @Test
    void testUpdateUserInfoNewUser() {
        UUID newUserId = UUID.randomUUID();
        UserDataJson inputUser = new UserDataJson(newUserId, "new_user", "NewFirstName",
                "NewLastName", "new_image.png");

        when(userDataRepository.findByUsername("new_user")).thenReturn(Optional.empty());

        UserDataEntity newUserEntity = new UserDataEntity();
        newUserEntity.setId(newUserId);
        newUserEntity.setUsername("new_user");
        newUserEntity.setFirstname(inputUser.firstname());
        newUserEntity.setLastname(inputUser.lastname());
        newUserEntity.setImage(inputUser.image() != null ? inputUser.image().getBytes(StandardCharsets.UTF_8) : null);

        when(userDataRepository.save(any(UserDataEntity.class))).thenReturn(newUserEntity);

        UserDataJson updatedUser = userDataService.update(inputUser);

        assertEquals(newUserId, updatedUser.id());
        assertEquals("new_user", updatedUser.username());
        assertEquals("NewFirstName", updatedUser.firstname());
        assertEquals("NewLastName", updatedUser.lastname());
        assertNotNull(updatedUser.image());
        assertEquals("new_image.png", updatedUser.image());
    }

    @Test
    void testGetCurrentUserFound() {
        UserDataEntity existingUser = new UserDataEntity();
        existingUser.setUsername("example_user12");
        existingUser.setFirstname("John");
        existingUser.setLastname("Doe");

        when(userDataRepository.findByUsername("example_user12")).thenReturn(Optional.of(existingUser));

        UserDataJson user = userDataService.getCurrentUser("example_user12");

        assertEquals("example_user12", user.username());
        assertEquals("John", user.firstname());
        assertEquals("Doe", user.lastname());
    }

    @Test
    void testGetCurrentUserNotFound() {
        String username = "non_existent_user";

        when(userDataRepository.findByUsername(username)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userDataService.getCurrentUser(username);
        });

        assertEquals("Can`t find user by username: '" + username + "'", exception.getMessage());
    }

    @Test
    void testListenerUserAlreadyExistsInDb() {
        String kafkaMessage = "{\"username\":\"existing_user\",\"firstname\":\"ExistingFirstName\",\"lastname\"" +
                ":\"ExistingLastName\",\"image\":\"existing_image.png\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("users", 0, 0L, "key", kafkaMessage);

        UserDataEntity existingUser = new UserDataEntity();
        existingUser.setUsername("existing_user");

        when(userDataRepository.findByUsername("existing_user")).thenReturn(Optional.of(existingUser));

        userDataService.listener(record);

        verify(userDataRepository, never()).save(any());

    }
}
