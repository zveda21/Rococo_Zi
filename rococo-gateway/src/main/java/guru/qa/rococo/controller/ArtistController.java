package guru.qa.rococo.controller;

import guru.qa.rococo.controller.client.ArtistClient;
import guru.qa.rococo.model.Artist;
import guru.qa.rococo.model.page.RestPage;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    private final ArtistClient client;

    public ArtistController(ArtistClient client) {
        this.client = client;
    }

    @GetMapping()
    public Page<Artist> getAll() {
        return new RestPage<>(client.getAll());
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