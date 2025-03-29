package guru.qa.rococo.model;

import java.util.UUID;

public record Museum(UUID id, String title, String description, String photo, GeoLocation geo) {
}
