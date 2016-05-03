package app.com.example.maverick.project1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by maverick on 30/4/16.
 */
public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder>{

    private ArrayList mArrayList;

    public ReviewRecyclerViewAdapter(ArrayList arrayList) {
        mArrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = (Review)mArrayList.get(position);

        holder.mAuthor.setText(review.getAuthor());
        holder.mContent.setText(review.getContent());

    }

    @Override
    public int getItemCount() {
        if(mArrayList.size() > 0)
            return mArrayList.size();
        else
            return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mAuthor;
        public final TextView mContent;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mAuthor = (TextView) itemView.findViewById(R.id.author);
            mContent = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
