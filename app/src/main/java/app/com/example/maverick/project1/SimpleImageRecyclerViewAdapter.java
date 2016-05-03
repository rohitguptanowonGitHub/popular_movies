package app.com.example.maverick.project1;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by maverick on 29/4/16.
 */
public class SimpleImageRecyclerViewAdapter extends RecyclerView.Adapter<SimpleImageRecyclerViewAdapter.ViewHolder> {

    private ArrayList marrayList;
    private Context mContext;
    private int mHeight;

    public SimpleImageRecyclerViewAdapter(Context context, ArrayList arrayList,int columnCount) {

        marrayList = arrayList;
        mContext = context;
        boolean isTablet = MainActivity.mTwoPane;

        Activity activity = (Activity) mContext;
        Display display = activity.getWindowManager().getDefaultDisplay();
       if(isTablet) {
           mHeight =(int)((display.getWidth()/columnCount)*0.75);
        }
        else{

            mHeight = (int)((display.getWidth()/columnCount)*1.5);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Movie movie = (Movie)marrayList.get(position);

        holder.mTextView.setText(movie.getTitle());

        Picasso.with(holder.mImageView.getContext())
                .load(movie.getMoviePoster())
                .resize(0,mHeight)
                .placeholder(R.drawable.movie)
                .into(holder.mImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ((CallBack)mContext).onItemSelected(position);
//                Intent intent = new Intent(mContext,MovieDetail.class);
//                intent.putExtra("index", position);
//                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(marrayList.size() > 0)
            return marrayList.size();
        else
            return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImageView = (ImageView)mView.findViewById(R.id.poster);
            mTextView  = (TextView)mView.findViewById(R.id.movie_title);
            itemView.setClickable(true);
        }
    }
    public interface CallBack {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(int index);
    }
}
