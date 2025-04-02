package guru.qa.rococo.service;

import guru.qa.rococo.exception.NotFoundException;
import guru.qa.rococo.model.Country;
import guru.qa.rococo.model.GeoLocation;
import guru.qa.rococo.model.Museum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MuseumServiceTest {

    @Autowired
    private MuseumService museumService;

    private Pageable pageable;

    @BeforeEach
    void setup() {
        pageable = Pageable.ofSize(10);
    }

    @Test
    @Sql("/insertMuseumData.sql")
    void shouldReturnAllMuseumListWhenTitleIsNull() {
        Page<Museum> museums = museumService.getAll(null, pageable);
        assertThat(museums.getContent()).hasSize(3);
    }

    @Test
    @Sql("/insertMuseumData.sql")
    void shouldReturnMuseumListWhenTitleIsNotNull() {
        Page<Museum> museums = museumService.getAll("british", pageable);

        List<Museum> content = museums.getContent();
        assertThat(content).hasSize(1);

        Museum museum = content.getFirst();

        assertThat(museum.title()).isEqualTo("British Museum");
        assertThat(museum.description()).isEqualTo("One of the largest museums in the world, featuring artifacts like the Rosetta Stone and Egyptian mummies.");
        assertThat(museum.geo().city()).isEqualTo("London");
        assertThat(museum.geo().country().name()).isEqualTo("United Kingdom");
        assertThat(museum.photo()).isNotNull();
    }

    @Test
    @Sql("/insertMuseumData.sql")
    void shouldReturnMuseumById() {
        Museum museum = museumService.findByMuseumId(UUID.fromString("4800753b-fd5a-46ce-b66e-03c64aa70990"));

        assertThat(museum.title()).isEqualTo("British Museum");
        assertThat(museum.description()).isEqualTo("One of the largest museums in the world, featuring artifacts like the Rosetta Stone and Egyptian mummies.");
        assertThat(museum.geo().city()).isEqualTo("London");
        assertThat(museum.geo().country().name()).isEqualTo("United Kingdom");
        assertThat(museum.photo()).isNotNull();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenMuseumNotFound() {
        final UUID nonExistentMuseumId = UUID.randomUUID();

        assertThatThrownBy(
                () -> museumService.findByMuseumId(nonExistentMuseumId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Museum not found " + nonExistentMuseumId);
    }

    @Test
    @Sql("/insertMuseumData.sql")
    void shouldUpdateMuseumWithSimpleFields() {
        final UUID museumId = UUID.fromString("4800753b-fd5a-46ce-b66e-03c64aa70990");
        Museum museum = museumService.findByMuseumId(museumId);

        final Museum update = new Museum(
                museum.id(),
                "New Title",
                "New Description",
                "New Photo",
                museum.geo());

        Museum updatedMuseum = museumService.update(update);

        assertThat(updatedMuseum.id()).isEqualTo(museumId);
        assertThat(updatedMuseum.title()).isEqualTo("New Title");
        assertThat(updatedMuseum.description()).isEqualTo("New Description");
        assertThat(updatedMuseum.photo()).isEqualTo("New Photo");
        assertThat(updatedMuseum.geo().city()).isEqualTo("London");
        assertThat(updatedMuseum.geo().country().name()).isEqualTo("United Kingdom");
    }

    @Test
    @Sql("/insertMuseumData.sql")
    void shouldUpdateMuseumWithoutPhoto() {
        final UUID museumId = UUID.fromString("4800753b-fd5a-46ce-b66e-03c64aa70990");
        Museum museum = museumService.findByMuseumId(museumId);

        final Museum update = new Museum(
                museum.id(),
                museum.title(),
                museum.description(),
                null,
                museum.geo());

        Museum updatedMuseum = museumService.update(update);

        assertThat(updatedMuseum.id()).isEqualTo(museumId);
        assertThat(updatedMuseum.title()).isEqualTo(museum.title());
        assertThat(updatedMuseum.description()).isEqualTo(museum.description());
        assertThat(updatedMuseum.photo()).isEqualTo(museum.photo());
        assertThat(updatedMuseum.geo().city()).isEqualTo("London");
        assertThat(updatedMuseum.geo().country().name()).isEqualTo("United Kingdom");
    }

    @Test
    @Sql("/insertMuseumData.sql")
    void shouldUpdateMuseumWithGeoLocation() {
        final UUID museumId = UUID.fromString("4800753b-fd5a-46ce-b66e-03c64aa70990");
        Museum museum = museumService.findByMuseumId(museumId);

        final Museum update = new Museum(
                museum.id(),
                museum.title(),
                museum.description(),
                museum.photo(),
                new GeoLocation("Paris", null));

        Museum updatedMuseum = museumService.update(update);

        assertThat(updatedMuseum.id()).isEqualTo(museumId);
        assertThat(updatedMuseum.title()).isEqualTo(museum.title());
        assertThat(updatedMuseum.description()).isEqualTo(museum.description());
        assertThat(updatedMuseum.photo()).isEqualTo(museum.photo());
        assertThat(updatedMuseum.geo().city()).isEqualTo("Paris");
        assertThat(updatedMuseum.geo().country().name()).isEqualTo("France");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenMuseumNotFoundById() {
        final UUID nonExistentMuseumId = UUID.randomUUID();
        final Museum museum = new Museum(
                nonExistentMuseumId,
                "New Title",
                "New Description",
                "New Content",
                new GeoLocation("New City", new Country(UUID.randomUUID(), "New Country")));

        assertThatThrownBy(
                () -> museumService.update(museum))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Museum not found " + nonExistentMuseumId);
    }

    @Test
    @Sql("/insertMuseumData.sql")
    void shouldThrowNotFoundExceptionWhenGeoLocationNotFound() {
        final UUID museumId = UUID.fromString("4800753b-fd5a-46ce-b66e-03c64aa70990");
        Museum museum = museumService.findByMuseumId(museumId);

        final Museum update = new Museum(
                museum.id(),
                museum.title(),
                museum.description(),
                museum.photo(),
                new GeoLocation("NonExistentCity", null));

        assertThatThrownBy(
                () -> museumService.update(update))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Location with city name NonExistentCity not found");
    }
}
