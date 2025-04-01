package qa.guru.rococo.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.okhttp3.AllureOkHttp3;
import lombok.SneakyThrows;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import qa.guru.rococo.config.Config;
import qa.guru.rococo.jupiter.extension.ApiLoginExtension;
import qa.guru.rococo.utils.OAuthUtils;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Optional;

public class AuthApiClient {
    private static final Config CONFIG = Config.getInstance();
    private AuthApi authApi;
    private String csrfToken;

    public AuthApiClient() {
        initialize();
    }

    private void initialize() {
        var builder = new OkHttpClient.Builder()
                .followRedirects(true);
        builder.addNetworkInterceptor(new CodeInterceptor());
        builder.addInterceptor(new okhttp3.logging.HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.HEADERS));
        builder.addNetworkInterceptor(new AllureOkHttp3());
        builder.cookieJar(
                new JavaNetCookieJar(
                        new CookieManager(
                                ThreadSafeCookieStore.INSTANCE,
                                CookiePolicy.ACCEPT_ALL
                        )
                )
        );

        var client = builder.build();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getInstance().authUrl())
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        this.authApi = retrofit.create(AuthApi.class);
    }

    @SneakyThrows
    public String login(String username, String password) {
        final String codeVerifier = OAuthUtils.generateCodeVerifier();
        final String codeChallenge = OAuthUtils.generateCodeChallange(codeVerifier);
        final String redirectUri = CONFIG.frontUrl() + "authorized";
        final String clientId = "client";

        authApi.authorize(
                "code",
                clientId,
                "openid",
                redirectUri,
                codeChallenge,
                "S256"
        ).execute();

        authApi.login(username, password, getCsrfToken()).execute();

        Response<JsonNode> tokenResponse = authApi.token(
                ApiLoginExtension.getCode(),
                redirectUri,
                clientId,
                codeVerifier,
                "authorization_code"
        ).execute();

        JsonNode body = tokenResponse.body();
        if (body == null) {
            throw new LoginAttemptException();
        }

        return body.get("id_token").asText();
    }

    @SneakyThrows
    public void register(String username, String password) {
        authApi.requestRegisterForm().execute();
        authApi.register(
                username,
                password,
                password,
                getCsrfToken()
        ).execute();
    }

    private String getCsrfToken() {
        Optional<String> csrf = ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN");
        csrf.ifPresent(s -> this.csrfToken = s);
        return csrfToken;
    }
}
