package guru.qa.rococo.controller;

import guru.qa.rococo.controller.client.ArtistClient;
import guru.qa.rococo.controller.client.MuseumClient;
import guru.qa.rococo.controller.client.PaintingClient;
import guru.qa.rococo.exception.InvalidRequestException;
import guru.qa.rococo.model.Artist;
import guru.qa.rococo.model.Museum;
import guru.qa.rococo.model.Painting;
import guru.qa.rococo.model.page.RestPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("unused")
@Slf4j
@RestController
@RequestMapping("/api/painting")
public class GatewayPaintingController {

    private final PaintingClient paintingClient;
    private final MuseumClient museumClient;
    private final ArtistClient artistClient;

    public GatewayPaintingController(PaintingClient paintingClient, MuseumClient museumClient, ArtistClient artistClient) {
        this.paintingClient = paintingClient;
        this.museumClient = museumClient;
        this.artistClient = artistClient;
    }

    @GetMapping()
    public Page<Painting> getAll(Pageable pageable, @RequestParam(required = false) String title) {
        List<Painting> paintings = paintingClient.getAll(pageable, title);
        return new RestPage<>(collectInfo(paintings));
    }

    @GetMapping("/{id}")
    public Painting findById(@PathVariable UUID id) {
        Painting painting = paintingClient.getById(id);
        // get artist and museum
        try {
            return collectInfo(painting);
        } catch (Exception e) {
            log.error("Error fetching painting info for {}", painting.id());
            return painting;
        }
    }

    @GetMapping("/author/{id}")
    public Page<Painting> getByAuthor(@PathVariable UUID id) {
        List<Painting> paintings = paintingClient.getByArtist(id);
        return new PageImpl<>(collectInfo(paintings));
    }

    @PostMapping
    Painting create(@RequestBody Painting painting) {
        validatePaintingDependencies(painting);
        try {
            Painting created = paintingClient.create(painting);
            return collectInfo(created);
        } catch (Exception e) {
            log.error("Error creating painting");
            throw new InvalidRequestException("Artist not found");
        }
    }

    @PatchMapping
    Painting update(@RequestBody Painting painting) {
        validatePaintingDependencies(painting);
        return collectInfo(paintingClient.update(painting));
    }

    private void validatePaintingDependencies(Painting painting) {
        // check artist
        if (Objects.nonNull(painting.artist()) && Objects.nonNull(painting.artist().id())) {
            try {
                var artist = artistClient.getById(painting.artist().id());
            } catch (Exception e) {
                throw new InvalidRequestException("Artist not found");
            }
        }

        // check museum
        if (Objects.nonNull(painting.museum()) && Objects.nonNull(painting.museum().id())) {
            try {
                var museum = museumClient.getById(painting.museum().id());
            } catch (Exception e) {
                throw new InvalidRequestException("Museum not found");
            }
        }
    }

    private List<Painting> collectInfo(List<Painting> paintings) {
        List<Painting> withInfo = new ArrayList<>();
        for (Painting painting : paintings) {
            try {
                withInfo.add(collectInfo(painting));
            } catch (Exception e) {
                log.error("Error fetching painting info for {}", painting.id());
                withInfo.add(painting);
            }
        }

        return withInfo;
    }

    private Painting collectInfo(Painting painting) {
        Artist artist = painting.artistId() == null ?
                Artist.ofEmpty() :
                artistClient.getById(painting.artistId());

        Museum museum = painting.museumId() == null ?
                Museum.ofEmpty() :
                museumClient.getById(painting.museumId());

        return Painting.withArtistAndMuseum(painting, artist, museum);
    }
}
