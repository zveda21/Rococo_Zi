package guru.qa.rococo.config;

import jakarta.annotation.Nonnull;

public enum DockerConfig implements Config {
    INSTANCE;

    @Nonnull
    @Override
    public String frontUrl() {
        return "http://frontend.rococo.dc/";
    }

    @Nonnull
    @Override
    public String authUrl() {
        return "http://auth.rococo.dc:9000/";
    }

    @Nonnull
    @Override
    public String authJdbcUrl() {
        return "jdbc:postgresql://rococo-all-db:5432/rococo-auth";
    }

    @Nonnull
    @Override
    public String userdataUrl() {
        return "http://userdata.rococo.dc:8089";
    }

    @Nonnull
    @Override
    public String userdataJdbcUrl() {
        return "jdbc:postgresql://rococo-all-db:5432/rococo-userdata";
    }

    @Nonnull
    @Override
    public String artistUrl() {
        return "http://artist.rococo.dc:8282";
    }

    @Nonnull
    @Override
    public String artistJdbcUrl() {
        return "jdbc:postgresql://rococo-all-db:5432/rococo-artist";
    }

    @Nonnull
    @Override
    public String museumUrl() {
        return "http://museum.rococo.dc:8283";
    }

    @Nonnull
    @Override
    public String museumJdbcUrl() {
        return "jdbc:postgresql://rococo-all-db:5432/rococo-museum";
    }

    @Nonnull
    @Override
    public String paintingUrl() {
        return "http://painting.rococo.dc:8484";
    }

    @Nonnull
    @Override
    public String paintingJdbcUrl() {
        return "jdbc:postgresql://rococo-all-db:5432/rococo-painting";
    }

    @Nonnull
    @Override
    public String gatewayUrl() {
        return "http://gateway.rococo.dc:8080";
    }
}
