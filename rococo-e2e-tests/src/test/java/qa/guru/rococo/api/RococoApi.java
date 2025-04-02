package qa.guru.rococo.api;

import qa.guru.rococo.model.rest.Artist;
import qa.guru.rococo.model.rest.Museum;
import qa.guru.rococo.model.rest.Painting;
import qa.guru.rococo.model.rest.page.RestPage;
import retrofit2.Call;
import retrofit2.http.*;

import javax.annotation.Nullable;

public interface RococoApi {
    @GET("api/artist")
    Call<RestPage<Artist>> getArtists(
            @Header("Authorization") String bearerToken,
            @Nullable @Query("name") String name,
            @Query("page") int page,
            @Query("size") int size);

    @GET("api/artist/{id}")
    Call<Artist> getArtistById(
            @Header("Authorization") String bearerToken,
            @Path("id") String id);

    @POST("api/artist")
    Call<Artist> createArtist(
            @Header("Authorization") String bearerToken,
            @Body Artist artist);

    @PATCH("/api/artist")
    Call<Artist> updateArtist(
            @Header("Authorization") String bearerToken,
            @Body Artist artist);

    @GET("api/museum")
    Call<RestPage<Museum>> getMuseums(
            @Header("Authorization") String bearerToken,
            @Nullable @Query("title") String title,
            @Query("page") int page,
            @Query("size") int size);

    @GET("api/museum/{id}")
    Call<Museum> getMuseumById(
            @Header("Authorization") String bearerToken,
            @Path("id") String id);

    @POST("api/museum")
    Call<Museum> createMuseum(
            @Header("Authorization") String bearerToken,
            @Body Museum museum);

    @PATCH("api/museum")
    Call<Museum> updateMuseum(
            @Header("Authorization") String bearerToken,
            @Body Museum museum);

    @GET("api/painting")
    Call<RestPage<Painting>> getPaintings(
            @Header("Authorization") String bearerToken,
            @Query("page") int page,
            @Query("size") int size);

    @GET("api/painting/{id}")
    Call<Painting> getPaintingById(
            @Header("Authorization") String bearerToken,
            @Path("id") String id);

    @GET("api/painting/author/{artistId}")
    Call<RestPage<Painting>> getPaintingByArtistId(
            @Header("Authorization") String bearerToken,
            @Path("artistId") String artistId);

    @POST("api/painting")
    Call<Painting> createPainting(
            @Header("Authorization") String bearerToken,
            @Body Painting painting);

    @PATCH("api/painting")
    Call<Painting> updatePainting(
            @Header("Authorization") String bearerToken,
            @Body Painting painting);
}
