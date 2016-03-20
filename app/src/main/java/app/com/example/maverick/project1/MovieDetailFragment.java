package app.com.example.maverick.project1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {
    TextView title, releaseDate, voteAverage, plotSynopsis;
    ImageView moviePoster;

    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Bundle bundle = getActivity().getIntent().getExtras();
        int index = bundle.getInt("index");
        Movie movie  = MainActivityFragment.movieArrayList.get(index);

        title = (TextView)rootView.findViewById(R.id.title);
        releaseDate = (TextView)rootView.findViewById(R.id.release_date);
        voteAverage = (TextView)rootView.findViewById(R.id.vote_average);
        plotSynopsis = (TextView)rootView.findViewById(R.id.plot);
        moviePoster = (ImageView)rootView.findViewById(R.id.movie_poster);

        title.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());
        voteAverage.setText(movie.getVoteAverage() + "/10");
        plotSynopsis.setText(movie.getPlotSynopsis());
        //moviePoster.setImageBitmap(movie.getMoviePosterBitmap());
        Picasso.with(getActivity()).load(movie.getMoviePoster()).into(moviePoster);
        
        return rootView;
    }
}
