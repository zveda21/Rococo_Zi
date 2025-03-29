package guru.qa.rococo.service;

import guru.qa.rococo.data.ArtistEntity;
import guru.qa.rococo.data.repository.ArtistRepository;
import guru.qa.rococo.ex.NotFoundException;
import guru.qa.rococo.model.ArtistJson;
import guru.qa.rococo.model.utils.StringAsBytes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    private ArtistService artistService;

    private ArtistEntity firstArtist;
    private ArtistEntity secondArtist;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        artistService = new ArtistService(artistRepository);
        pageable = PageRequest.of(0, 10);
        firstArtist = new ArtistEntity();
        firstArtist.setId(UUID.randomUUID());
        firstArtist.setName("Claude Monet");
        firstArtist.setBiography("Famous French painter");
        firstArtist.setPhoto(new byte[0]);
        secondArtist = new ArtistEntity();
        secondArtist.setId(UUID.randomUUID());
        secondArtist.setName("Vincent van Gogh");
        secondArtist.setBiography("A Dutch Post-Impressionist painter");
        secondArtist.setPhoto(new byte[0]);
    }

    @Test
    void shouldReturnArtistsPageWhenNameIsNull() {
        Page<ArtistEntity> artistPage = new PageImpl<>(Arrays.asList(firstArtist, secondArtist), pageable, 2);
        when(artistRepository.findAll(pageable)).thenReturn(artistPage);

        Page<ArtistJson> artistsJsonPage = artistService.getAll(null, pageable);

        assertThat(artistsJsonPage.getTotalElements()).isEqualTo(2);
        assertThat(artistsJsonPage.getContent()).hasSize(2);
    }

    @Test
    void shouldSearchByNameWhenNameIsProvided() {
        String searchName = "Claude Monet";
        Page<ArtistEntity> artistPage = new PageImpl<>(Collections.singletonList(firstArtist), pageable, 1);
        when(artistRepository.findAllByNameContainsIgnoreCase(searchName, pageable)).thenReturn(artistPage);

        Page<ArtistJson> artistsJsonPage = artistService.getAll(searchName, pageable);

        assertThat(artistsJsonPage.getTotalElements()).isEqualTo(1);
        assertThat(artistsJsonPage.getContent()).hasSize(1);
        assertThat(artistsJsonPage.getContent().getFirst().name()).isEqualTo(searchName);
    }

    @Test
    void shouldReturnEmptyPageWhenNoArtistsMatchSearchName() {
        String searchName = "Nonexistent Artist";
        Page<ArtistEntity> artistPage = new PageImpl<>(List.of(), pageable, 0);
        when(artistRepository.findAllByNameContainsIgnoreCase(searchName, pageable)).thenReturn(artistPage);

        Page<ArtistJson> artistsJsonPage = artistService.getAll(searchName, pageable);

        assertThat(artistsJsonPage.getTotalElements()).isEqualTo(0);
        assertThat(artistsJsonPage.getContent()).isEmpty();
    }

    @Test
    void shouldReturnArtistWhenArtistExists() throws Exception {
        when(artistRepository.findById(firstArtist.getId())).thenReturn(Optional.of(firstArtist));

        ArtistJson artistJson = artistService.findArtistById(firstArtist.getId().toString());

        assertThat(artistJson).isNotNull();
        assertThat(artistJson.id()).isEqualTo(firstArtist.getId());
        assertThat(artistJson.name()).isEqualTo("Claude Monet");
        assertThat(artistJson.biography()).isEqualTo("Famous French painter");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenArtistDoesNotExist() {
        String nonExistentArtistId = UUID.randomUUID().toString();
        when(artistRepository.findById(UUID.fromString(nonExistentArtistId))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> artistService.findArtistById(nonExistentArtistId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("The artist not found by id: " + nonExistentArtistId);
    }

    @Test
    void shouldAddArtist() {
        ArtistJson newArtistJson = new ArtistJson(UUID.randomUUID(), "Vincent van Gogh",
                "Famous Dutch post-impressionist painter", "photoBase64");

        ArtistEntity newArtistEntity = new ArtistEntity();
        newArtistEntity.setId(newArtistJson.id());
        newArtistEntity.setName(newArtistJson.name());
        newArtistEntity.setBiography(newArtistJson.biography());
        newArtistEntity.setPhoto(new StringAsBytes(newArtistJson.photo()).bytes());

        when(artistRepository.save(any(ArtistEntity.class))).thenReturn(newArtistEntity);

        ArtistJson addedArtist = artistService.add(newArtistJson);

        assertThat(addedArtist).isNotNull();
        assertThat(addedArtist.name()).isEqualTo("Vincent van Gogh");
        assertThat(addedArtist.biography()).isEqualTo("Famous Dutch post-impressionist painter");
        assertThat(addedArtist.photo()).isEqualTo("photoBase64");
    }

    @Test
    void shouldUpdateArtistWhenArtistExists() throws Exception {
        UUID artistId = UUID.randomUUID();
        ArtistEntity existingArtist = new ArtistEntity();
        existingArtist.setId(artistId);
        existingArtist.setName("Claude Monet");
        existingArtist.setBiography("Original biography");
        existingArtist.setPhoto(new byte[0]);
        ArtistJson updatedArtistJson = new ArtistJson(artistId, "Claude Monet - Updated",
                "Updated biography", "updatedPhotoInBase64");

        when(artistRepository.findById(artistId)).thenReturn(Optional.of(existingArtist));
        when(artistRepository.save(any(ArtistEntity.class))).thenReturn(existingArtist);

        ArtistJson updatedArtist = artistService.update(updatedArtistJson);

        assertThat(updatedArtist).isNotNull();
        assertThat(updatedArtist.id()).isEqualTo(artistId);
        assertThat(updatedArtist.name()).isEqualTo("Claude Monet - Updated");
        assertThat(updatedArtist.biography()).isEqualTo("Updated biography");
        assertThat(updatedArtist.photo()).isEqualTo("updatedPhotoInBase64");
    }
}
