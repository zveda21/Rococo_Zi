package guru.qa.rococo.controller;

import guru.qa.rococo.data.Artist;
import guru.qa.rococo.model.page.RestPage;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    // todo implement a RestClient and fetch from rococo-artist api

    @GetMapping()
    public Page<Artist> getAll() {
        var artist1 = new Artist(UUID.randomUUID(), "a", "lorem ipsum", "a.png");
        var artist2 = new Artist(UUID.randomUUID(), "b", "lorem ipsum", "b.png");
        return new RestPage<>(List.of(artist1, artist2));
    }

    @GetMapping("/{id}")
    public Artist findArtistById(@PathVariable("id") String id) {
        return new Artist(UUID.randomUUID(), "a", "lorem ipsum", "a.png");
    }

    @PatchMapping()
    public Artist updateArtist(@RequestBody Artist artist) {
        return artist;
    }

    @PostMapping()
    public Artist addArtist(@RequestBody Artist artist) {
        return artist;
    }
}