package guru.qa.rococo.controller;

import guru.qa.rococo.controller.client.MuseumClient;
import guru.qa.rococo.model.Museum;
import guru.qa.rococo.model.page.RestPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/museum")
public class MuseumController {

    private final MuseumClient client;

    public MuseumController(MuseumClient client) {
        this.client = client;
    }

    @GetMapping()
    public Page<Museum> getAll(Pageable pageable,  @RequestParam(required = false) String title) {
        return new RestPage<>(client.getAll(pageable, title));
    }

    @GetMapping("/{id}")
    public Museum findByMuseumId(@PathVariable UUID id) {
        return client.getById(id);
    }

    @PatchMapping
    Museum update(@RequestBody Museum museum) {
        return client.update(museum);
    }

    @PostMapping
    Museum create(@RequestBody Museum museum) {
        return client.create(museum);
    }
}
