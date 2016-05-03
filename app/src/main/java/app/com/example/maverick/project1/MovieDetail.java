package app.com.example.maverick.project1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putInt(MovieDetailFragment.DETAIL_INDEX,getIntent().getExtras().getInt(MovieDetailFragment.DETAIL_INDEX));

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                MovieDetailFragment fragment =(MovieDetailFragment)fm.findFragmentById(R.id.movie_detail_container);

                long inserted = fragment.addMovie();
                if (inserted > 0){
                    Snackbar.make(view, "Added to Favourites !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fab.setImageResource(R.drawable.ic_favorite_black_24dp);
                }
                else if(inserted < 0)
                    Snackbar.make(view, "Error !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else {
                    Snackbar.make(view, "Already in Favourites !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
