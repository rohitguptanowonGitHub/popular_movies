package app.com.example.maverick.project1;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by maverick on 29/4/16.
 */
public interface IMovieService {
    @GET("3/movie/{categories}")
    retrofit.Call<MovieInfo> getMovieInfo(@Path("categories")String categories,@Query("api_key") String apiKey);

    @GET("3/movie/{id}/reviews")
    retrofit.Call<MovieReview> getMovieReview(@Path("id")String movieId,@Query("api_key")String apiKey);

    @GET("3/movie/{id}/videos")
    retrofit.Call<MovieTrailer> getMovieTrailer(@Path("id")String movieId, @Query("api_key") String apiKey);
}