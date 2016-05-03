package app.com.example.maverick.project1;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by maverick on 29/4/16.
 */
public class RetrofitManager {
    IMovieService iMovieService = null;
    public RetrofitManager(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        iMovieService = retrofit.create(IMovieService.class);

    }
    public void getMovieData(String sortCriteria,String apiKey,Callback<MovieInfo> callback){
        Call<MovieInfo> call = iMovieService.getMovieInfo(sortCriteria, apiKey);
        call.enqueue(callback);
    }
    public void getReviewData(String movieId, String apiKey,Callback<MovieReview>callback){
        Call<MovieReview> call = iMovieService.getMovieReview(movieId, apiKey);
        call.enqueue(callback);
    }
    public void getTrailerData(String movieId, String apiKey, Callback<MovieTrailer> callback){
        Call<MovieTrailer> call = iMovieService.getMovieTrailer(movieId, apiKey);
        call.enqueue(callback);
    }
}
