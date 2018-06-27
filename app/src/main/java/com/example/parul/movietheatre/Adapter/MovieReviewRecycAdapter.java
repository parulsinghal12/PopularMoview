package com.example.parul.movietheatre.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parul.movietheatre.Data.ReviewDataItem;
import com.example.parul.movietheatre.Data.TrailerDataItem;
import com.example.parul.movietheatre.R;

import java.util.ArrayList;

public class MovieReviewRecycAdapter extends RecyclerView.Adapter<MovieReviewRecycAdapter.ReviewItemViewHolder>{

    private ArrayList<ReviewDataItem> mReviewList = new ArrayList<>();
    private final Context mCtx;
    private final ListItemClickListener mItemClickListener;


    public MovieReviewRecycAdapter(Context ctx , ListItemClickListener listener ){
        mCtx = ctx;
        mItemClickListener = listener;
    }

    @Override
    public ReviewItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean shouldAttachToParentImmediately = false;
        View view = LayoutInflater.from(mCtx).inflate(R.layout.review_list_item,parent,shouldAttachToParentImmediately);
        return new ReviewItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewItemViewHolder holder, int position) {
        //ctx = get

        holder.mAuthor.setText(mReviewList.get(position).getAuthor());
        holder.mComments.setText(mReviewList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if (mReviewList == null || mReviewList.size() == 0) return 0;
        return mReviewList.size();
    }

    public void setMovieData(ArrayList<ReviewDataItem> movieDataList){
        mReviewList = movieDataList;
        notifyDataSetChanged();
    }

    public class ReviewItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView mAuthor;
        final TextView mComments;
        ReviewItemViewHolder(View itemView) {
            super(itemView);
            mAuthor = itemView.findViewById(R.id.tv_author);
            mComments = itemView.findViewById(R.id.tv_review);
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            mItemClickListener.onClick(mReviewList.get(getAdapterPosition()));
        }

    }

    public interface ListItemClickListener {
        void onClick(ReviewDataItem item);

    }


}

