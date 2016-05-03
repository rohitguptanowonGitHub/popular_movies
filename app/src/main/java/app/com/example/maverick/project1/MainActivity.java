package app.com.example.maverick.project1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements SimpleImageRecyclerViewAdapter.CallBack{
    static boolean mTwoPane ;
    Bundle args;
    private static final String DETAILFRAGMENT_TAG ="DF_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        if(findViewById(R.id.movie_detail_container) != null){
            mTwoPane = true;

            if(savedInstanceState == null){
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(),DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane =false;
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(!mTwoPane)
            fab.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                MovieDetailFragment fragment = (MovieDetailFragment) fm.findFragmentById(R.id.movie_detail_container);
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
                startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(int index) {
        if(mTwoPane){
            args =new Bundle();
                args.putInt(MovieDetailFragment.DETAIL_INDEX, index);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container,fragment,DETAILFRAGMENT_TAG)
                    .commit();
        }else {
            Intent intent = new Intent(this,MovieDetail.class)
                    .putExtra(MovieDetailFragment.DETAIL_INDEX,index);
            startActivity(intent);
        }
    }

}
