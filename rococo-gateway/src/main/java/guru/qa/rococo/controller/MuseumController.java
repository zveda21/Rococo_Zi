package guru.qa.rococo.controller;

import guru.qa.rococo.controller.client.MuseumClient;
import guru.qa.rococo.model.Museum;
import guru.qa.rococo.model.page.RestPage;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/museum")
public class MuseumController {

    private final MuseumClient client;

    public MuseumController(MuseumClient client) {
        this.client = client;
    }

    @GetMapping()
    public Page<Museum> getAll(@RequestParam(required = false) String title) {
        return new RestPage<>(client.getAll());
    }
}
