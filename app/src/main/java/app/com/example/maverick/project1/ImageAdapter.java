package app.com.example.maverick.project1;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by maverick on 10/3/16.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList marrayList;
    private Movie movie;

    public ImageAdapter(Context c, ArrayList arrayList){
        mContext = c;
        marrayList = arrayList;
    }
    @Override
    public int getCount() {
        return marrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ImageView imageView;
        if (view == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            view = imageView;
            } else {
            imageView = (ImageView) view;
        }

        movie = null;
        movie = (Movie)marrayList.get(i);

        Picasso.with(mContext).load(movie.getMoviePoster()).resize(0,thumbnailHeight()).into(/*new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.v("Bit", "this isn't working");
                imageView.setImageBitmap(bitmap);
                movie.setMoviePosterBitmap(bitmap);

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        }*/imageView);

        return view;
    }
    public int thumbnailHeight(){

        Activity activity = (Activity)mContext;
        Display display = activity.getWindowManager().getDefaultDisplay();

        if(activity.getResources().getConfiguration().orientation == 1)
            MainActivityFragment.mGridView.setColumnWidth(display.getWidth()/2);
        else
            MainActivityFragment.mGridView.setColumnWidth(display.getWidth()/3);

        return (int)(MainActivityFragment.mGridView.getColumnWidth()*1.5);
    }

}
