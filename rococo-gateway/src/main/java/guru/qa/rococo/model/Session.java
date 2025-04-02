package guru.qa.rococo.model;

import java.util.Date;

public record Session(
        String username,
        Date issuedAt,
        Date expiresAt) {
    public static Session empty() {
        return new Session(null, null, null);
    }
}
