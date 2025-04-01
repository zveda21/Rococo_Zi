package guru.qa.rococo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.rococo.controller.client.ArtistClient;
import guru.qa.rococo.controller.client.MuseumClient;
import guru.qa.rococo.controller.client.PaintingClient;
import guru.qa.rococo.exception.InvalidRequestException;
import guru.qa.rococo.exception.RemoteServerException;
import guru.qa.rococo.model.Artist;
import guru.qa.rococo.model.Museum;
import guru.qa.rococo.model.Painting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GatewayPaintingControllerTest {

    @Mock
    private PaintingClient paintingClient;

    @Mock
    private MuseumClient museumClient;

    @Mock
    private ArtistClient artistClient;

    @InjectMocks
    private GatewayPaintingController gatewayPaintingController;

    private UUID paintingId;
    private UUID artistId;
    private UUID museumId;
    private Painting painting;
    private Artist artist;
    private Museum museum;

    private static <T> T getMockObject(Resource resource, Class<T> clazz) {
        try {
            String json = Constants.getResponse(resource);
            return new ObjectMapper().readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read mock object", e);
        }
    }

    @BeforeEach
    void setUp() {
        paintingId = UUID.fromString(Constants.Painting.ID_1);
        artistId = UUID.fromString(Constants.Artist.ID_1);
        museumId = UUID.fromString(Constants.Museum.ID_1);

        artist = getMockObject(new ClassPathResource("response/internal/artist/get-one.json"), Artist.class);
        museum = getMockObject(new ClassPathResource("response/internal/museum/get-one.json"), Museum.class);
        painting = getMockObject(new ClassPathResource("response/internal/painting/get-one.json"), Painting.class);
    }

    @Test
    void testGetAll() {
        when(paintingClient.getAll(Pageable.ofSize(10), null)).thenReturn(List.of(painting));
        Page<Painting> result = gatewayPaintingController.getAll(Pageable.ofSize(10), null);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        verify(paintingClient).getAll(Pageable.ofSize(10), null);
    }

    @Test
    void testFindById() {
        when(paintingClient.getById(paintingId)).thenReturn(painting);
        when(artistClient.getById(artistId)).thenReturn(artist);
        when(museumClient.getById(museumId)).thenReturn(museum);

        Painting result = gatewayPaintingController.findById(paintingId);
        assertNotNull(result);
        assertEquals("Starry Night", result.title());
    }

    @Test
    void testFindById_WithExceptionHandling() {
        when(paintingClient.getById(paintingId)).thenReturn(painting);
        doThrow(new RuntimeException("Service unavailable"))
                .when(artistClient).getById(artistId);

        Painting result = gatewayPaintingController.findById(paintingId);
        assertNotNull(result);
        assertEquals(painting, result);
    }

    @Test
    void testCreate() {
        Painting payload = Painting.withArtistAndMuseum(painting, artist, museum);
        when(artistClient.getById(artistId)).thenReturn(artist);
        when(museumClient.getById(museumId)).thenReturn(museum);
        when(paintingClient.create(payload)).thenReturn(payload);

        Painting result = gatewayPaintingController.create(payload);
        assertNotNull(result);
        assertEquals("Starry Night", result.title());
    }


    @Test
    void testCreate_InvalidArtist() {
        lenient().when(artistClient.getById(artistId)).thenThrow(new RemoteServerException(new HttpClientErrorException(HttpStatus.BAD_REQUEST)));

        assertThrows(InvalidRequestException.class, () -> gatewayPaintingController.create(painting));
    }

    @Test
    void testUpdate() {
        Painting payload = Painting.withArtistAndMuseum(painting, artist, museum);
        when(artistClient.getById(artistId)).thenReturn(artist);
        when(museumClient.getById(museumId)).thenReturn(museum);
        when(paintingClient.update(payload)).thenReturn(payload);

        Painting result = gatewayPaintingController.update(payload);
        assertNotNull(result);
        assertEquals("Starry Night", result.title());
    }

    @Test
    void testGetByAuthor() {
        when(paintingClient.getByArtist(artistId)).thenReturn(List.of(painting));
        Page<Painting> result = gatewayPaintingController.getByAuthor(artistId);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        verify(paintingClient).getByArtist(artistId);
    }
}
