package guru.qa.rococo.controller;

import guru.qa.rococo.model.Painting;
import guru.qa.rococo.service.PaintingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/internal/painting")
public class PaintingController {
    private final PaintingService service;

    @Autowired
    public PaintingController(PaintingService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Painting> getAll(Pageable pageable, @RequestParam(required = false) String title) {
        Pageable sortedPage = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Order.asc("title")));
        return service.getAll(title, sortedPage);
    }

    @GetMapping("/{id}")
    public Painting getById(@PathVariable UUID id) {
        return service.findByPaintingId(id);
    }

    @GetMapping("/artist/{id}")
    public Page<Painting> getByArtistId(Pageable pageable, @PathVariable UUID id) {
        return service.findByArtistId(id, pageable);
    }

    @PostMapping
    public Painting create(@RequestBody Painting painting) {
        return service.create(painting);
    }

    @PatchMapping
    public Painting update(@RequestBody Painting painting) {
        return service.update(painting);
    }
}
