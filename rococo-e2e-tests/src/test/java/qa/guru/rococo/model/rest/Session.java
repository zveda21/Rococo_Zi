package qa.guru.rococo.model.rest;

import java.util.Date;

public record Session(
        String username,
        Date issuedAt,
        Date expiresAt) {
    public static Session empty() {
        return new Session(null, null, null);
    }
}
