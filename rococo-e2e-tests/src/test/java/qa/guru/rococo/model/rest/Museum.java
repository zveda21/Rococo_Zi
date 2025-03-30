package qa.guru.rococo.model.rest;

import java.util.UUID;

public record Museum(UUID id, String title, String description, String photo, GeoLocation geo) {
    public static Museum ofEmpty() {
        return new Museum(null, null, null, null, null);
    }
}
