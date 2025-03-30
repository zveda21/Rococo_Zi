package qa.guru.rococo.api;

import qa.guru.rococo.model.rest.Artist;
import qa.guru.rococo.model.rest.Museum;
import qa.guru.rococo.model.rest.Painting;
import qa.guru.rococo.model.rest.page.RestPage;
import retrofit2.Call;
import retrofit2.http.*;

public interface RococoApi {
    @GET("api/artist")
    Call<RestPage<Artist>> getArtists(
            @Header("Authorization") String bearerToken);

    @GET("api/artist/{id}")
    Call<Artist> getArtistById(
            @Header("Authorization") String bearerToken,
            @Path("id") String id);

    @POST("api/artist")
    Call<Artist> createArtist(
            @Header("Authorization") String bearerToken,
            @Body Artist artist);

    @GET("api/museum")
    Call<RestPage<Museum>> getMuseums(
            @Header("Authorization") String bearerToken);

    @GET("api/museum/{id}")
    Call<Museum> getMuseumById(
            @Header("Authorization") String bearerToken,
            @Path("id") String id);

    @GET("api/painting")
    Call<RestPage<Painting>> getPaintings(
            @Header("Authorization") String bearerToken);

    @GET("api/painting/{id}")
    Call<Painting> getPaintingById(
            @Header("Authorization") String bearerToken,
            @Path("id") String id);
}
