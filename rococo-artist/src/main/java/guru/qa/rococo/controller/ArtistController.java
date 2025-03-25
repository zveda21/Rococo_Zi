package guru.qa.rococo.controller;

import guru.qa.rococo.model.ArtistJson;
import guru.qa.rococo.service.ArtistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/artist")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public Page<ArtistJson> getAllArtists(Pageable pageable, @RequestParam(required = false) String name) {
        return artistService.getAll(name, pageable);
    }

    @GetMapping("/{id}")
    public ArtistJson getArtistById(@PathVariable("id") String id) {
        return artistService.findArtistById(id);
    }
}
