package qa.guru.rococo.config;

enum DockerConfig implements Config {
    INSTANCE;

    @Override
    public String profile() {
        return "docker";
    }

    @Override
    public String frontUrl() {
        return "http://frontend.rococo.dc/";
    }

    @Override
    public String gatewayUrl() {
        return "http://gateway.rococo.dc:8080/";
    }

    @Override
    public String authUrl() {
        return "http://auth.rococo.dc:9000/";
    }

    @Override
    public String allureDockerServiceUrl() {
        return "";
    }
}
