package qa.guru.rococo.api;

import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import qa.guru.rococo.config.Config;
import qa.guru.rococo.model.rest.Artist;
import qa.guru.rococo.model.rest.Museum;
import qa.guru.rococo.model.rest.Painting;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RococoApiClient {
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().gatewayUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final RococoApi rococoApi = retrofit.create(RococoApi.class);

    @SneakyThrows
    public Page<Artist> getArtists(String bearerToken) {
        return rococoApi.getArtists(bearerToken).execute().body();
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
    public Page<Painting> getPaintings(String bearerToken) {
        return rococoApi.getPaintings(bearerToken).execute().body();
    }

    @SneakyThrows
    public Painting getPaintingById(String bearerToken, String id) {
        return rococoApi.getPaintingById(bearerToken, id).execute().body();
    }

    @SneakyThrows
    public Page<Museum> getMuseums(String bearerToken) {
        return rococoApi.getMuseums(bearerToken).execute().body();
    }

    @SneakyThrows
    public Museum getMuseumById(String bearerToken, String id) {
        return rococoApi.getMuseumById(bearerToken, id).execute().body();
    }
}
