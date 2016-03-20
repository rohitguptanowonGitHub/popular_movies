package app.com.example.maverick.project1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ImageAdapter mImageAdapter;
    static public GridView mGridView;
    static ArrayList<Movie> movieArrayList = new ArrayList<Movie>();
    private ArrayList<Movie> movieList;

    String sort_order;

    public MainActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies",movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies"))
            movieList = new ArrayList<Movie>(movieArrayList);
        else
            movieList = savedInstanceState.getParcelableArrayList("movies");

        mImageAdapter = new ImageAdapter(getActivity(),movieArrayList);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView)rootView.findViewById(R.id.gridview_layout);
        mGridView.setAdapter(mImageAdapter);


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(),MovieDetail.class);
                intent.putExtra("index",i);
                startActivity(intent);

            }
        });


        return rootView;
    }

    private void moviesUpdate(){
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sort_order = prefs.getString("sort","popular");
        moviesTask.execute(sort_order);
    }


    @Override
    public void onStart() {
        super.onStart();
        moviesUpdate();
    }

    public class FetchMoviesTask extends AsyncTask<String,Void,ArrayList>{

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            if(mImageAdapter == null){
                mImageAdapter = new ImageAdapter(getActivity(),arrayList);
                mImageAdapter.notifyDataSetChanged();
            }else{
                mImageAdapter.notifyDataSetChanged();
            }
            mGridView.setAdapter(mImageAdapter);
            super.onPostExecute(arrayList);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String...params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;

            //String sort_by ="popularity.desc";

            try{
                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie?";
                final String API_KEY = "api_key";
                //final String SORT_PARAM ="sort_by";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(params[0])
                        //.appendQueryParameter(SORT_PARAM,params[0])
                        .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null)
                    return null;

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line+"\n");
                }
                if(buffer.length() == 0){
                    return null;
                }
                moviesJsonStr = buffer.toString();


            } catch (IOException e){
                Log.e(LOG_TAG,"error",e);
                return null;
            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    }catch (final IOException e){
                        Log.e(LOG_TAG,"Error closing stream",e);
                        e.printStackTrace();
                    }
                }
            }
            try {
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e){
                Log.e(LOG_TAG,e.getMessage(),e);
                e.printStackTrace();
            }
            return null;
        }

        private ArrayList<Movie> getMoviesDataFromJson(String moviesJsonStr)
                throws JSONException{
            final String TMD_RESULT = "results";
            final String TMD_TITLE = "title";
            final String TMD_RELEASE_DATE = "release_date";
            final String TMD_MOVIE_POSTER = "poster_path";
            final String TMD_VOTE_AVERAGE = "vote_average";
            final String TMD_PLOT_SYNOPSIS = "overview";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(TMD_RESULT);

            movieArrayList.clear();

            for(int i = 0 ; i < moviesArray.length() ; i++){
                String title;
                String release_date;
                String movies_poster_address;
                String vote_average;
                String plot_snopsis;

                JSONObject movieJson = moviesArray.getJSONObject(i);

                title = movieJson.getString(TMD_TITLE);
                release_date = movieJson.getString(TMD_RELEASE_DATE);
                movies_poster_address = movieJson.getString(TMD_MOVIE_POSTER);
                vote_average = movieJson.getString(TMD_VOTE_AVERAGE);
                plot_snopsis = movieJson.getString(TMD_PLOT_SYNOPSIS);

                StringBuilder movies_poster = new StringBuilder(movies_poster_address);
                movies_poster.deleteCharAt(0);
                String release_year = release_date.substring(0,4);

                Movie movie = new Movie();

                movie.setTitle(title);
                movie.setReleaseDate(release_year);
                movie.setMoviePoster("http://image.tmdb.org/t/p/w342/" + movies_poster);
                movie.setVoteAverage(vote_average);
                movie.setPlotSynopsis(plot_snopsis);

                movieArrayList.add(movie);
            }
            return movieArrayList;
        }
    }
}
