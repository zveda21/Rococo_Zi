package guru.qa.rococo.config;

import jakarta.annotation.Nonnull;

public enum LocalConfig implements Config {
    INSTANCE;

    @Nonnull
    @Override
    public String gatewayUrl() {
        return "http://127.0.0.1:8080/";
    }

    @Nonnull
    @Override
    public String frontUrl() {
        return "http://127.0.0.1:3000/";
    }

    @Nonnull
    @Override
    public String authUrl() {
        return "http://127.0.0.1:9000/";
    }

    @Nonnull
    @Override
    public String authJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/rococo-auth";
    }

    @Nonnull
    @Override
    public String userdataUrl() {
        return "http://127.0.0.1:8089/";
    }

    @Nonnull
    @Override
    public String userdataJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/rococo-userdata";
    }

    @Nonnull
    @Override
    public String artistUrl() {
        return "http://127.0.0.1:8282/";
    }

    @Nonnull
    @Override
    public String artistJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/rococo-artist'";
    }

    @Nonnull
    @Override
    public String museumUrl() {
        return "http://127.0.0.1:8383/";
    }

    @Nonnull
    @Override
    public String museumJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/rococo-museum";
    }

    @Nonnull
    @Override
    public String paintingUrl() {
        return "http://127.0.0.1:8484/";
    }

    @Nonnull
    @Override
    public String paintingJdbcUrl() {
        return "jdbc:postgresql://127.0.0.1:5432/rococo-painting";
    }
}
