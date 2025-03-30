package qa.guru.rococo.config;

enum LocalConfig implements Config {
    INSTANCE;

    @Override
    public String frontUrl() {
        return "http://127.0.0.1:3000/";
    }

    @Override
    public String gatewayUrl() {
        return "http://127.0.0.1:8080/";
    }

    @Override
    public String authUrl() {
        return "http://127.0.0.1:9000/";
    }

    @Override
    public String defaultUsername() {
        return EnvVars.ROCOCO_DEFAULT_USERNAME.get();
    }

    @Override
    public String defaultPassword() {
        return EnvVars.ROCOCO_DEFAULT_PASSWORD.get();
    }
}
