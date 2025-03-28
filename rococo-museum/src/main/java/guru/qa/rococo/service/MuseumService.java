package guru.qa.rococo.service;

import guru.qa.rococo.data.MuseumEntity;
import guru.qa.rococo.data.repository.MuseumRepository;
import guru.qa.rococo.model.Museum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MuseumService {
    private final MuseumRepository repository;

    @Autowired
    public MuseumService(MuseumRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Museum> getAll(String title, Pageable pageable) {
        Page<MuseumEntity> entities = title == null ?
                repository.findAll(pageable) :
                repository.findByTitleContainingIgnoreCase(pageable, title);
        return entities.map(Museum::ofEntity);
    }
}
