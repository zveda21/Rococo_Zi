package qa.guru.rococo.config;

public enum EnvVars {
    PROFILE("PROFILE"),
    ROCOCO_LOGIN_RETRY("ROCOCO_LOGIN_RETRY"),
    ROCOCO_LOGIN_BACKOFF_MS("ROCOCO_LOGIN_BACKOFF_MS");

    private final String name;

    EnvVars(String name) {
        this.name = name;
    }

    public String get() {
        return System.getenv(name);
    }
}
