package guru.qa.rococo.service;

import guru.qa.rococo.data.PaintingEntity;
import guru.qa.rococo.data.repository.PaintingRepository;
import guru.qa.rococo.model.Painting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PaintingServiceTest {

    @Mock
    private PaintingRepository paintingRepository;

    private PaintingService paintingService;

    private UUID firstArtistId;
    private UUID secondArtistId;

    private UUID firstMuseumId;
    private UUID secondMuseumId;

    private PaintingEntity firstPainting;
    private PaintingEntity secondPainting;
    private PaintingEntity thirdPainting;

    private Pageable pageable;

    @BeforeEach
    void setup() {
        paintingService = new PaintingService(paintingRepository);
        pageable = PageRequest.of(0, 10);

        firstArtistId = UUID.randomUUID();
        secondArtistId = UUID.randomUUID();
        firstMuseumId = UUID.randomUUID();
        secondMuseumId = UUID.randomUUID();

        // init a list of PaintingEntity
        firstPainting = new PaintingEntity();
        firstPainting.setId(UUID.randomUUID());
        firstPainting.setTitle("Starry Night");
        firstPainting.setDescription("A famous painting by Vincent van Gogh");
        firstPainting.setContent(new byte[0]);
        firstPainting.setArtistId(firstArtistId);
        firstPainting.setMuseumId(firstMuseumId);

        secondPainting = new PaintingEntity();
        secondPainting.setId(UUID.randomUUID());
        secondPainting.setTitle("The Persistence of Memory");
        secondPainting.setDescription("A famous painting by Salvador Dalí");
        secondPainting.setContent(new byte[0]);
        secondPainting.setArtistId(secondArtistId);
        secondPainting.setMuseumId(secondMuseumId);

        thirdPainting = new PaintingEntity();
        thirdPainting.setId(UUID.randomUUID());
        thirdPainting.setTitle("The Scream");
        thirdPainting.setDescription("A famous painting by Edvard Munch");
        thirdPainting.setContent(new byte[0]);
        thirdPainting.setArtistId(firstArtistId);
        thirdPainting.setMuseumId(secondMuseumId);
    }

    @Test
    void shouldReturnAllPaintingsWhenTitleIsNull() {
        Page<PaintingEntity> result = new PageImpl<>(List.of(firstPainting, secondPainting, thirdPainting));
        when(paintingRepository.findAll(pageable)).thenReturn(result);

        Page<Painting> paintings = paintingService.getAll(null, pageable);

        assertThat(paintings.getTotalElements()).isEqualTo(3);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Starry Night", "starry night", "STARry NIGHT", "STAR"})
    void shouldReturnPaintingsByTitle(String token) {
        final String title = "Starry Night";
        Page<PaintingEntity> result = new PageImpl<>(List.of(firstPainting));

        when(paintingRepository.findByTitleContainingIgnoreCase(pageable, token)).thenReturn(result);

        Page<Painting> paintings = paintingService.getAll(token, pageable);

        assertThat(paintings.getTotalElements()).isEqualTo(1);
        assertThat(paintings.getContent().getFirst().title()).isEqualTo(title);
    }

    @Test
    void shouldReturnEmptyPageWhenNoPaintingsMatchTitle() {
        String title = "Nonexistent Painting";
        Page<PaintingEntity> result = new PageImpl<>(List.of());
        when(paintingRepository.findByTitleContainingIgnoreCase(pageable, title)).thenReturn(result);

        Page<Painting> paintings = paintingService.getAll(title, pageable);

        assertThat(paintings.getTotalElements()).isEqualTo(0);
        assertThat(paintings.getContent()).isEmpty();
    }

    @Test
    void shouldReturnPaintingsByArtistId() {
        Page<PaintingEntity> result = new PageImpl<>(List.of(firstPainting, thirdPainting));
        when(paintingRepository.findByArtistId(pageable, firstArtistId)).thenReturn(result);

        Page<Painting> paintings = paintingService.findByArtistId(firstArtistId, pageable);

        assertThat(paintings.getTotalElements()).isEqualTo(2);
        assertThat(paintings.getContent()).hasSize(2);

        paintings.getContent().stream().map(Painting::title).forEach(title -> {
            assertThat(title).isIn(firstPainting.getTitle(), thirdPainting.getTitle());
        });
    }

    @Test
    void shouldReturnEmptyPageWhenNoPaintingsMatchArtistId() {
        Page<PaintingEntity> result = new PageImpl<>(List.of());
        when(paintingRepository.findByArtistId(pageable, firstArtistId)).thenReturn(result);

        Page<Painting> paintings = paintingService.findByArtistId(firstArtistId, pageable);

        assertThat(paintings.getTotalElements()).isEqualTo(0);
        assertThat(paintings.getContent()).isEmpty();
    }

    @Test
    void shouldReturnPaintingById() {
        when(paintingRepository.findById(firstPainting.getId())).thenReturn(Optional.of(firstPainting));

        Painting painting = paintingService.findByPaintingId(firstPainting.getId());

        assertThat(painting.id()).isEqualTo(firstPainting.getId());
        assertThat(painting.title()).isEqualTo(firstPainting.getTitle());
        assertThat(painting.description()).isEqualTo(firstPainting.getDescription());
    }

    @Test
    void shouldUpdatePainting() {
        Painting updated = new Painting(firstPainting.getId(), "Updated Title", "Updated Description", "A", firstArtistId, firstMuseumId);

        PaintingEntity updatedEntity = new PaintingEntity();
        updatedEntity.setId(firstPainting.getId());
        updatedEntity.setTitle("Updated Title");
        updatedEntity.setDescription("Updated Description");
        updatedEntity.setContent(new byte[] {65});
        updatedEntity.setArtistId(firstArtistId);
        updatedEntity.setMuseumId(firstMuseumId);

        when(paintingRepository.findById(firstPainting.getId())).thenReturn(Optional.of(firstPainting));
        when(paintingRepository.save(eq(firstPainting))).thenReturn(updatedEntity);

        Painting painting = paintingService.update(updated);

        assertThat(painting.id()).isEqualTo(updatedEntity.getId());
        assertThat(painting.title()).isEqualTo(updatedEntity.getTitle());
        assertThat(painting.description()).isEqualTo(updatedEntity.getDescription());
        assertThat(painting.content()).isEqualTo("A");
    }

    @Test
    void shouldNotUpdateContentWhenItIsNull() {
        Painting updated = new Painting(firstPainting.getId(), "Updated Title", "Updated Description", null, firstArtistId, firstMuseumId);

        when(paintingRepository.findById(firstPainting.getId())).thenReturn(Optional.of(firstPainting));
        when(paintingRepository.save(eq(firstPainting))).thenReturn(firstPainting);

        Painting painting = paintingService.update(updated);

        assertThat(painting.id()).isEqualTo(firstPainting.getId());
        assertThat(painting.title()).isEqualTo(firstPainting.getTitle());
        assertThat(painting.description()).isEqualTo(firstPainting.getDescription());
        assertThat(painting.content()).isNull();
    }

    @Test
    void shouldThrowExceptionWhenPaintingNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        Painting updated = new Painting(nonExistentId, "Updated Title", "Updated Description", null, firstArtistId, firstMuseumId);

        when(paintingRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paintingService.update(updated))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Painting not found");
    }

    @Test
    void shouldCreatePainting() {
        PaintingEntity newPainting = new PaintingEntity();
        newPainting.setId(null);
        newPainting.setTitle("New Painting");
        newPainting.setDescription("New Description");
        newPainting.setContent(new byte[0]);
        newPainting.setArtistId(firstArtistId);
        newPainting.setMuseumId(firstMuseumId);

        when(paintingRepository.save(any(PaintingEntity.class))).thenReturn(newPainting);

        Painting painting = paintingService.create(Painting.ofEntity(newPainting));

        assertThat(painting.id()).isEqualTo(newPainting.getId());
        assertThat(painting.title()).isEqualTo(newPainting.getTitle());
        assertThat(painting.description()).isEqualTo(newPainting.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenFieldsAreInvalid() {
        Painting painting = new Painting(null, null, null, null, null, null);

        assertThatThrownBy(() -> paintingService.create(painting))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Title should not be null", "Description should not be null");
    }
}
