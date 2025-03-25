package guru.qa.rococo.data;

import java.util.UUID;

public record Artist(UUID id, String name, String biography, String photo) {
}
