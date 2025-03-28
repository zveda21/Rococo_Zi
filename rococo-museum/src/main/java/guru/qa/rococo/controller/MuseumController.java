package guru.qa.rococo.controller;

import guru.qa.rococo.model.Museum;
import guru.qa.rococo.service.MuseumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
