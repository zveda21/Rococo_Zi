package qa.guru.rococo.config;

import java.util.Objects;

public interface Config {

    static Config getInstance() {
        return LocalConfig.INSTANCE;
    }

    String frontUrl();

    String gatewayUrl();

    String authUrl();

    default String defaultUsername() {
        return EnvVars.ROCOCO_DEFAULT_USERNAME.get();
    }

    default String defaultPassword() {
        return EnvVars.ROCOCO_DEFAULT_PASSWORD.get();
    }

    default Integer loginRetry() {
        return Integer.parseInt(
                Objects.requireNonNullElse(EnvVars.ROCOCO_LOGIN_RETRY.get(), "10")
        );
    }

    default Long loginBackoffMs() {
        return Long.parseLong(
                Objects.requireNonNullElse(EnvVars.ROCOCO_LOGIN_BACKOFF_MS.get(), "3000")
        );
    }
}
