package guru.qa.rococo.controller;

import guru.qa.rococo.model.Museum;
import guru.qa.rococo.service.MuseumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/internal/museum")
public class MuseumController {
    private final MuseumService service;

    public MuseumController(MuseumService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Museum> getAll(Pageable pageable, @RequestParam(required = false) String title) {
        return service.getAll(title, pageable);
    }

    @GetMapping("/{id}")
    public Museum getById(@PathVariable UUID id) {
        return service.findByMuseumId(id);
    }
}
