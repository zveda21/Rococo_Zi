package guru.qa.rococo.service;

import guru.qa.rococo.data.ArtistEntity;
import guru.qa.rococo.data.repository.ArtistRepository;
import guru.qa.rococo.ex.NotFoundException;
import guru.qa.rococo.model.ArtistJson;
import guru.qa.rococo.model.utils.StringAsBytes;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ArtistService {
    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Transactional(readOnly = true)
    public @Nonnull Page<ArtistJson> getAll(@Nullable String name, @Nonnull Pageable pageable) {
        Page<ArtistEntity> artists = (name == null)
                ? artistRepository.findAll(pageable)
                : artistRepository.findAllByNameContainsIgnoreCase(name, pageable);
        return artists.map(ArtistJson::fromEntity);
    }

    @Transactional(readOnly = true)
    public @Nonnull ArtistJson findArtistById(@Nonnull String id) {
        return ArtistJson.fromEntity(
                artistRepository.findById(
                        UUID.fromString(id)
                ).orElseThrow(
                        () -> new NotFoundException("The artist not found by id: " + id)
                )
        );
    }

    @Transactional
    public @Nonnull ArtistJson update(@Nonnull ArtistJson artist) {
        ArtistEntity artistEntity = getRequiredArtist(artist.id());
        artistEntity.setName(artist.name());
        artistEntity.setBiography(artist.biography());
        artistEntity.setPhoto(
                new StringAsBytes(
                        artist.photo()
                ).bytes()
        );
        return ArtistJson.fromEntity(
                artistRepository.save(artistEntity)
        );
    }

    @Transactional
    public ArtistJson add(ArtistJson artist) {
        return ArtistJson.fromEntity(
                artistRepository.save(
                        artist.toEntity()
                )
        );
    }

    private @Nonnull ArtistEntity getRequiredArtist(@Nonnull UUID id) {
        return artistRepository.findById(id).orElseThrow(
                () -> new NotFoundException("The artist not found by id: " + id)
        );
    }
}