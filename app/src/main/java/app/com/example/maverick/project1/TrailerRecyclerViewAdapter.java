package app.com.example.maverick.project1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by maverick on 1/5/16.
 */
public class TrailerRecyclerViewAdapter extends RecyclerView.Adapter<TrailerRecyclerViewAdapter.ViewHolder>{
    private ArrayList trailerArrayList;
    private Context mContext;

    public TrailerRecyclerViewAdapter(ArrayList arrayList, Context context) {
        trailerArrayList = arrayList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Trailer trailer = (Trailer) trailerArrayList.get(position);
        Picasso.with(holder.mThumbnail.getContext())
                .load("http://img.youtube.com/vi/" + trailer.getMkey() + "/default.jpg")
                .placeholder(R.drawable.placeholder)
                .into(holder.mThumbnail);

        holder.mName.setText(trailer.getmName());
        holder.mType.setText(trailer.getmType());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri videoUri = Uri.parse("http://www.youtube.com/")
                        .buildUpon()
                        .appendPath("watch")
                        .appendQueryParameter("v",trailer.getMkey())
                        .build();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(videoUri);
                if(intent.resolveActivity(mContext.getPackageManager())!= null)
                    mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(trailerArrayList.size() > 0)
            return trailerArrayList.size();
        else
            return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mThumbnail;
        public final TextView mName, mType;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mThumbnail = (ImageView)itemView.findViewById(R.id.thumbnail);
            mName = (TextView)itemView.findViewById(R.id.video_name);
            mType = (TextView)itemView.findViewById(R.id.video_type);
        }
    }
}
