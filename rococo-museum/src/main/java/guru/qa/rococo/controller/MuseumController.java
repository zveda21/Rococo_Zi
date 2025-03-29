package guru.qa.rococo.controller;

import guru.qa.rococo.model.Museum;
import guru.qa.rococo.service.MuseumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Pageable sortedPage = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Order.asc("title")));
        return service.getAll(title, sortedPage);
    }

    @GetMapping("/{id}")
    public Museum getById(@PathVariable UUID id) {
        return service.findByMuseumId(id);
    }

    @PatchMapping
    public Museum update(@RequestBody Museum museum) {
        return service.update(museum);
    }
}
