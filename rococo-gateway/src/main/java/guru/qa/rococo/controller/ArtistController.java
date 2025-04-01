package guru.qa.rococo.controller;

import guru.qa.rococo.controller.client.ArtistClient;
import guru.qa.rococo.model.Artist;
import guru.qa.rococo.model.page.RestPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    private final ArtistClient client;

    public ArtistController(ArtistClient client) {
        this.client = client;
    }

    @GetMapping()
    public Page<Artist> getAll(Pageable pageable, @RequestParam(required = false) String name) {
        return new RestPage<>(client.getAll(pageable, name));
    }

    @GetMapping("/{id}")
    public Artist findArtistById(@PathVariable("id") String id) {
        return client.getById(UUID.fromString(id));
    }

    @PatchMapping()
    public Artist updateArtist(@RequestBody Artist artist) {
        return client.update(artist);
    }

    @PostMapping()
    public Artist addArtist(@RequestBody Artist artist) {
        return client.create(artist);
    }
}