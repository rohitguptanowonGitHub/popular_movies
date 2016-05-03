package app.com.example.maverick.project1;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;

import app.com.example.maverick.project1.data.MovieContract;
import retrofit.Callback;
import retrofit.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    static public RecyclerView mRecyclerView;
    static ArrayList<Movie> movieArrayList = new ArrayList<Movie>();
    static ArrayList<Movie> arrayList = new ArrayList<Movie>();
    static RetrofitManager manager = new RetrofitManager();
    String latestSortParams;

    public MainActivityFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getCurrentSortParam(getActivity()).equals(getString(R.string.pref_sort_fav))){
            showFavourites();
        }
        else if(paramsChanged()){
            moviesUpdate();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", movieArrayList);
        outState.putString("latestParams", getCurrentSortParam(getActivity()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_main, container, false);


        if( savedInstanceState!=null && savedInstanceState.containsKey("movies") && !paramsChanged())
            setupRecyclerView(mRecyclerView, savedInstanceState.getIntegerArrayList("movies"));


        return mRecyclerView;
    }
    public void moviesUpdate() {

            if(isNetworkAvailable()){
                String sortParam = getCurrentSortParam(getActivity());
                getMoviesFromWeb(sortParam, BuildConfig.THE_MOVIE_DB_API_KEY);
            }
            else
                Toast.makeText(getActivity(),"No Connection !",Toast.LENGTH_SHORT).show();
    }


    private void getMoviesFromWeb(String sortCriteria, String apiKey) {
        Callback<MovieInfo> callback = new Callback<MovieInfo>() {
            @Override
            public void onResponse(Response<MovieInfo> response) {
                if (response.isSuccess()) {
                    movieArrayList.clear();
                    movieArrayList.addAll(response.body().movieArrayList);
                    if(movieArrayList.size() > 0)
                       setupRecyclerView(mRecyclerView, movieArrayList);
                    if(MainActivity.mTwoPane){
                        MovieDetailFragment fragment = (MovieDetailFragment) getFragmentManager().findFragmentById(R.id.movie_detail_container);
                        fragment.fillLayout();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
        manager.getMovieData(sortCriteria, apiKey, callback);
    }

    private void setupRecyclerView(RecyclerView recyclerView, ArrayList list) {

        int columnCount;

        if (getResources().getConfiguration().orientation == 1)
            columnCount = 2;
        else
            columnCount = 3;

        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), columnCount));
        recyclerView.setAdapter(new SimpleImageRecyclerViewAdapter(getContext(), list, columnCount));
    }

    public void showFavourites(){
        Cursor cursor = getActivity().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        Vector<ContentValues> cVVector = new Vector<ContentValues>(cursor.getCount());
        if(cursor.moveToFirst()){
            do{
                ContentValues cv = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor,cv);
                cVVector.add(cv);
            }while (cursor.moveToNext());
        }

        arrayList = new ArrayList<Movie>(cVVector.size());
        for(int i = 0 ; i< cVVector.size();i++) {
            ContentValues movieValues = cVVector.elementAt(i);
            Movie movie = new Movie();
            movie.setTitle(movieValues.getAsString(MovieContract.MovieEntry.COLUMN_TITLE));
            movie.setMoviePoster(movieValues.getAsString(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
            movie.setReleaseDate(movieValues.getAsString(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
            movie.setVoteAverage(movieValues.getAsString(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE));
            movie.setPlotSynopsis(movieValues.getAsString(MovieContract.MovieEntry.COLUMN_OVERVIEW));

            arrayList.add(i, movie);
        }
        if(arrayList != null){
            setupRecyclerView(mRecyclerView,arrayList);
        }
        else
            Toast.makeText(getActivity(),"No movies in Collection yet !",Toast.LENGTH_SHORT).show();
    }

    public static String getCurrentSortParam(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_key),context.getString(R.string.pref_sort_default));
    }
    public boolean paramsChanged(){
        if(latestSortParams == null)
            return true;
        return !latestSortParams.equals(getCurrentSortParam(getActivity()));
    }
    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

}