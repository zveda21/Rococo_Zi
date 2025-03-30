package qa.guru.rococo.config;

public interface Config {

    static Config getInstance() {
        return LocalConfig.INSTANCE;
    }

    String frontUrl();

    String gatewayUrl();

    String authUrl();

    String defaultUsername();

    String defaultPassword();
}
