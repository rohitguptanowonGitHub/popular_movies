package app.com.example.maverick.project1;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Vector;

import app.com.example.maverick.project1.data.MovieContract;
import retrofit.Callback;
import retrofit.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {
    static final String DETAIL_INDEX = "INDEX";
    TextView title, releaseDate, voteAverage, plotSynopsis,reviewTag,trailerTag;
    RatingBar ratingBar;
    RecyclerView mReviewList, mTrailerList;
    ImageView moviePoster;
    ShareActionProvider mShareActionProvider;
    Movie movie;
    ArrayList<Review> reviewArrayList= new ArrayList<Review>();
    ArrayList<Trailer> trailerArrayList = new ArrayList<Trailer>();
    String movieTitle;
    private int index;
    String sortParam = "";

    public MovieDetailFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        String sort = MainActivityFragment.getCurrentSortParam(getActivity());
        if(!sort.equals(sortParam))
            index = 0;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(menuItem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if(arguments!=null) {
            index = arguments.getInt(DETAIL_INDEX);
        }

        View rootView = inflater.inflate(R.layout.detail_view, container, false);
        setHasOptionsMenu(true);

        title = (TextView)rootView.findViewById(R.id.title);
        releaseDate = (TextView)rootView.findViewById(R.id.release_date);
        voteAverage = (TextView)rootView.findViewById(R.id.vote_average);
        plotSynopsis = (TextView)rootView.findViewById(R.id.plot);
        moviePoster = (ImageView)rootView.findViewById(R.id.movie_poster);
        mReviewList = (RecyclerView)rootView.findViewById(R.id.review_list);
        mTrailerList = (RecyclerView)rootView.findViewById(R.id.trailer_list);
        reviewTag = (TextView)rootView.findViewById(R.id.review_tag);
        trailerTag = (TextView)rootView.findViewById(R.id.trailer_tag);
        ratingBar = (RatingBar)rootView.findViewById(R.id.rating);

        if(MainActivityFragment.getCurrentSortParam(getActivity()).equals(getActivity().getString(R.string.pref_sort_fav))) {
                if(MainActivityFragment.arrayList.size()>0)
                    fillLayoutForFav();

        }
        else{
            if(MainActivityFragment.movieArrayList.size()>0){
                fillLayout();
            }
        }
        return rootView;
    }
    public void fillLayout(){
            movie  = MainActivityFragment.movieArrayList.get(index);
            getReviewFromWeb(movie.getId(), BuildConfig.THE_MOVIE_DB_API_KEY);
            getTrailerFromWeb(movie.getId(), BuildConfig.THE_MOVIE_DB_API_KEY);
            title.setText(movie.getTitle());
            releaseDate.setText(movie.getReleaseDate());
            String vAvg = getString(R.string.rating_format,Float.valueOf(movie.getVoteAverage()));
            voteAverage.setText(vAvg);
            ratingBar.setRating(Float.valueOf(movie.getVoteAverage())/2);
            plotSynopsis.setText(movie.getPlotSynopsis());
            Picasso.with(getActivity()).load(movie.getMoviePoster()).placeholder(R.drawable.movie).into(moviePoster);

    }
    public void fillLayoutForFav(){
                movie  = MainActivityFragment.arrayList.get(index);
                title.setText(movie.getTitle());
                releaseDate.setText(movie.getReleaseDate());
                String vAvg = getString(R.string.rating_format,Float.valueOf(movie.getVoteAverage()));
                Picasso.with(getActivity()).load(movie.getMoviePoster()).placeholder(R.drawable.movie).into(moviePoster);
                ratingBar.setRating(Float.valueOf(movie.getVoteAverage()) / 2);
                voteAverage.setText(vAvg);
                plotSynopsis.setText(movie.getPlotSynopsis());
                reviewTag.setText("");
                trailerTag.setText("");
                reviewArrayList.clear();
                trailerArrayList.clear();
                mReviewList.setAdapter(new ReviewRecyclerViewAdapter(reviewArrayList));
                mTrailerList.setAdapter(new TrailerRecyclerViewAdapter(trailerArrayList,getContext()));

    }

    public void getReviewFromWeb(String movieId,String apikey){
        Callback<MovieReview> callback = new Callback<MovieReview>() {
            @Override
            public void onResponse(Response<MovieReview> response) {
                if(response.isSuccess()){
                    reviewArrayList.clear();
                    reviewArrayList.addAll(response.body().reviewArrayList);
                    if(reviewArrayList.size()>0)
                    {
                        mReviewList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mReviewList.setAdapter(new ReviewRecyclerViewAdapter(reviewArrayList));
                    }
                    else {
                        reviewTag.setText("No Reviews yet!");
                    }
                }
            }
            @Override
            public void onFailure(Throwable t) {

            }
        };
        MainActivityFragment.manager.getReviewData(movieId, apikey, callback);
    }

    public void getTrailerFromWeb(String movieId,String apiKey){
        Callback<MovieTrailer> callback = new Callback<MovieTrailer>() {
            @Override
            public void onResponse(Response<MovieTrailer> response) {
                if(response.isSuccess()){
                    trailerArrayList.clear();
                    trailerArrayList.addAll(response.body().trailerArrayList);
                    if(trailerArrayList.size()>0){
                        mTrailerList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mTrailerList.setAdapter(new TrailerRecyclerViewAdapter(trailerArrayList, getActivity()));
                        if(mShareActionProvider != null && !MainActivityFragment.getCurrentSortParam(getActivity()).equals("favourite"))
                            mShareActionProvider.setShareIntent(createTrailerShareIntent());

                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        };
        MainActivityFragment.manager.getTrailerData(movieId, apiKey, callback);
    }
    public Intent createTrailerShareIntent(){
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            Uri videoUri = Uri.parse("http://www.youtube.com/")
                .buildUpon()
                .appendPath("watch")
                .appendQueryParameter("v", trailerArrayList.get(0).getMkey())
                .build();
        shareIntent.putExtra(Intent.EXTRA_TEXT, videoUri.toString() + "\n" + movie.getTitle() + "\n" + trailerArrayList.get(0).getmName());
        return shareIntent;
    }

    long checkForDbEntry(String title){
        long movieId;
        Cursor movieCursor = getActivity().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry._ID},
                MovieContract.MovieEntry.COLUMN_TITLE + " = ? ",
                new String[]{title},
                null
        );
        if(movieCursor.moveToFirst()){
            int movieIdIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry._ID);
            movieId = movieCursor.getLong(movieIdIndex);
            movieCursor.close();
            return movieId;

        }
        else
            return -1;
    }

    long addMovie(){
        long movieId = 0;

        if(checkForDbEntry(movie.getTitle()) == -1 ) {
            ContentValues movieValues = new ContentValues();

            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
            movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getMoviePoster());
            movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
            movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getPlotSynopsis());

            Uri insertedMovieUri = getActivity().getContentResolver().insert(
                    MovieContract.MovieEntry.CONTENT_URI,
                    movieValues
            );
            movieId = ContentUris.parseId(insertedMovieUri);


            Vector<ContentValues> reviewVector = new Vector<ContentValues>(reviewArrayList.size());
            for (int i = 0; i < reviewArrayList.size(); i++) {

                ContentValues reviewValues = new ContentValues();

                reviewValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_KEY, movieId);
                reviewValues.put(MovieContract.ReviewEntry.COLUMN_AUTOR, reviewArrayList.get(i).getAuthor());
                reviewValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT, reviewArrayList.get(i).getContent());

                reviewVector.add(reviewValues);
            }
            int insertedR = 0;
            if (reviewVector.size() > 0) {
                ContentValues[] reviewArray = new ContentValues[reviewVector.size()];
                reviewVector.toArray(reviewArray);
                insertedR = getActivity().getContentResolver().bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, reviewArray);
            }

            Vector<ContentValues> trailerVector = new Vector<ContentValues>(trailerArrayList.size());
            for (int i = 0; i < trailerArrayList.size(); i++) {

                ContentValues reviewValues = new ContentValues();

                reviewValues.put(MovieContract.TrailerEntry.COLUMN_MOVIE_KEY, movieId);
                reviewValues.put(MovieContract.TrailerEntry.COLUMN_THUMBNAIL,/*getDrawableFromUrl*/("http://www.youtube.com/watch?v=" + trailerArrayList.get(i).getMkey()));
                reviewValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME, trailerArrayList.get(i).getmName());
                reviewValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_TYPE, trailerArrayList.get(i).getmType());

                reviewVector.add(reviewValues);
            }
            int insertedT = 0;
            if (reviewVector.size() > 0) {
                ContentValues[] reviewArray = new ContentValues[reviewVector.size()];
                reviewVector.toArray(reviewArray);
                insertedT = getActivity().getContentResolver().bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, reviewArray);
            }
        }
        return movieId;
    }

    void getOfflineData(){
        Cursor cursor = getActivity().getContentResolver().query(
                MovieContract.MovieEntry.buildMovieUriFromTitle(movieTitle),
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToLast();

        Vector<ContentValues> cVVector = new Vector<ContentValues>(cursor.getCount());

        if(cursor.moveToFirst()){
            do{
                ContentValues cv = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, cv);
                cVVector.add(cv);
            }while (cursor.moveToNext());
        }
        cursor.moveToFirst();
        ArrayList<Review> reviewArrayList = new ArrayList<Review>();
        ArrayList<Trailer>trailerArrayList = new ArrayList<Trailer>();
        for(int i = 0 ; i< cVVector.size();i++){
            ContentValues movieValues = cVVector.elementAt(i);
            Review review = new Review();
            Trailer trailer = new Trailer();

            review.setAuthor(movieValues.getAsString(MovieContract.ReviewEntry.COLUMN_AUTOR));
            review.setContent(movieValues.getAsString(MovieContract.ReviewEntry.COLUMN_CONTENT));

            trailer.setMkey(movieValues.getAsString(MovieContract.TrailerEntry.COLUMN_THUMBNAIL));
            trailer.setmName(movieValues.getAsString(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME));
            trailer.setmType(movieValues.getAsString(MovieContract.TrailerEntry.COLUMN_TRAILER_TYPE));

            if(reviewArrayList.contains(review))
                continue;
            else
            reviewArrayList.add(review);

            if(trailerArrayList.contains(trailer))
                continue;
            else
            trailerArrayList.add(trailer);
        }

        mReviewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mReviewList.setAdapter(new ReviewRecyclerViewAdapter(reviewArrayList));

        mTrailerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTrailerList.setAdapter(new TrailerRecyclerViewAdapter(trailerArrayList, getActivity()));
    }
//    public String imageDownload(String url) {
//        Picasso.with(getActivity())
//                .load(url)
//                .into(new Target() {
//                          @Override
//                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                              try {
//
//                                  File file = new File(getActivity().getFilesDir(),"img.jpg");
//
//                                  FileOutputStream out = new FileOutputStream(file);
//                                  bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//
//                                  out.flush();
//                                  out.close();
//                              } catch(Exception e){
//                                  // some action
//                              }
//                          }
//
//                          @Override
//                          public void onBitmapFailed(Drawable errorDrawable) {
//                          }
//
//                          @Override
//                          public void onPrepareLoad(Drawable placeHolderDrawable) {
//                          }
//                      }
//                );
//        return getActivity().getFilesDir()+"img.jpg";
//    }
}
