package qa.guru.rococo.api;

import io.qameta.allure.okhttp3.AllureOkHttp3;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import org.springframework.data.domain.Page;
import qa.guru.rococo.config.Config;
import qa.guru.rococo.model.rest.Artist;
import qa.guru.rococo.model.rest.Museum;
import qa.guru.rococo.model.rest.Painting;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.time.Duration;

public class RococoApiClient {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 100;

    private static final OkHttpClient.Builder builder = new OkHttpClient.Builder();
    static {
        builder.addNetworkInterceptor(new AllureOkHttp3());
        builder.readTimeout(Duration.ofSeconds(30));
    }

    private final OkHttpClient client = builder.build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().gatewayUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .client(client)
            .build();

    private final RococoApi rococoApi = retrofit.create(RococoApi.class);

    @SneakyThrows
    public Page<Artist> getArtists(String bearerToken) {
        return rococoApi.getArtists(bearerToken, null, DEFAULT_PAGE, DEFAULT_PAGE_SIZE).execute().body();
    }

    @SneakyThrows
    public Page<Artist> getArtists(String bearerToken, String name) {
        return rococoApi.getArtists(bearerToken, name, DEFAULT_PAGE, DEFAULT_PAGE_SIZE).execute().body();
    }

    @SneakyThrows
    public Page<Artist> getArtists(String bearerToken, int size) {
        return rococoApi.getArtists(bearerToken, null, DEFAULT_PAGE, size).execute().body();
    }

    @SneakyThrows
    public Artist getArtistById(String bearerToken, String id) {
        return rococoApi.getArtistById(bearerToken, id).execute().body();
    }

    @SneakyThrows
    public Artist createArtist(String bearerToken, Artist artist) {
        return rococoApi.createArtist(bearerToken, artist).execute().body();
    }

    @SneakyThrows
    public Artist updateArtist(String bearerToken, Artist artist) {
        return rococoApi.updateArtist(bearerToken, artist).execute().body();
    }

    @SneakyThrows
    public Page<Painting> getPaintings(String bearerToken, String title) {
        return rococoApi.getPaintings(bearerToken, title, DEFAULT_PAGE, DEFAULT_PAGE_SIZE).execute().body();
    }

    @SneakyThrows
    public Page<Painting> getPaintings(String bearerToken, int size) {
        return rococoApi.getPaintings(bearerToken, null, DEFAULT_PAGE, size).execute().body();
    }

    @SneakyThrows
    public Painting getPaintingById(String bearerToken, String id) {
        return rococoApi.getPaintingById(bearerToken, id).execute().body();
    }

    @SneakyThrows
    public Page<Painting> getPaintingByArtistId(String bearerToken, String artistId) {
        return rococoApi.getPaintingByArtistId(bearerToken, artistId).execute().body();
    }

    @SneakyThrows
    public Page<Museum> getMuseums(String bearerToken, String title) {
        return rococoApi.getMuseums(bearerToken, title, DEFAULT_PAGE, DEFAULT_PAGE_SIZE).execute().body();
    }

    @SneakyThrows
    public Page<Museum> getMuseums(String bearerToken, int size) {
        return rococoApi.getMuseums(bearerToken, null, DEFAULT_PAGE, size).execute().body();
    }

    @SneakyThrows
    public Museum getMuseumById(String bearerToken, String id) {
        return rococoApi.getMuseumById(bearerToken, id).execute().body();
    }

    @SneakyThrows
    public Museum createMuseum(String bearerToken, Museum museum) {
        return rococoApi.createMuseum(bearerToken, museum).execute().body();
    }

    @SneakyThrows
    public Museum updateMuseum(String bearerToken, Museum museum) {
        return rococoApi.updateMuseum(bearerToken, museum).execute().body();
    }

    @SneakyThrows
    public Painting createPainting(String bearerToken, Painting painting) {
        return rococoApi.createPainting(bearerToken, painting).execute().body();
    }

    @SneakyThrows
    public Painting updatePainting(String bearerToken, Painting painting) {
        return rococoApi.updatePainting(bearerToken, painting).execute().body();
    }
}
