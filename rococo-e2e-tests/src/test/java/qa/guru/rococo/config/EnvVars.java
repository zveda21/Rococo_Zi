package qa.guru.rococo.config;

public enum EnvVars {
    ROCOCO_DEFAULT_USERNAME("ROCOCO_DEFAULT_USERNAME"),
    ROCOCO_DEFAULT_PASSWORD("ROCOCO_DEFAULT_PASSWORD");

    private final String name;

    EnvVars(String name) {
        this.name = name;
    }

    public String get() {
        return System.getenv(name);
    }
}
