package guru.qa.rococo.controller;

import guru.qa.rococo.service.api.RestArtistClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    private final RestArtistClient restArtistClient;

    @Autowired
    public ArtistController(RestArtistClient restArtistClient) {
        this.restArtistClient = restArtistClient;
    }

//    @GetMapping()
//    public Page<ArtistJson> getAll(@RequestParam(required = false) String name,
//                                   @PageableDefault Pageable pageable) {
//        return restArtistClient.getAll(name, pageable);
//    }
//
//    @GetMapping("/{id}")
//    public ArtistJson findArtistById(@PathVariable("id") String id) {
//        return artistService.findArtistById(id);
//    }
//
//    @PatchMapping()
//    public ArtistJson updateArtist(@RequestBody ArtistJson artist) {
//        return artistService.update(artist);
//    }
//
//    @PostMapping()
//    public ArtistJson addArtist(@RequestBody ArtistJson artist) {
//        return artistService.add(artist);
//    }
}