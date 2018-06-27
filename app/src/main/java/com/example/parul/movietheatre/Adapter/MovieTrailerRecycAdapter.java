package com.example.parul.movietheatre.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parul.movietheatre.Data.TrailerDataItem;
import com.example.parul.movietheatre.R;

import java.util.ArrayList;

public class MovieTrailerRecycAdapter extends RecyclerView.Adapter<MovieTrailerRecycAdapter.VideoItemViewHolder>{

    private ArrayList<TrailerDataItem> mTrailerList = new ArrayList<>();
    private final Context mCtx;
    private final ListItemClickListener mItemClickListener;


    public MovieTrailerRecycAdapter(Context ctx , ListItemClickListener listener ){
        mCtx = ctx;
        mItemClickListener = listener;
    }

    @Override
    public VideoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean shouldAttachToParentImmediately = false;
        View view = LayoutInflater.from(mCtx).inflate(R.layout.trailer_list_item,parent,shouldAttachToParentImmediately);
        return new VideoItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoItemViewHolder holder, int position) {
        //ctx = get

        holder.mTrailerName.setText(mTrailerList.get(position).getTrailerName());
    }

    @Override
    public int getItemCount() {
        if (mTrailerList == null || mTrailerList.size() == 0) return 0;
        return mTrailerList.size();
    }

    public void setMovieData(ArrayList<TrailerDataItem> movieDataList){
        mTrailerList = movieDataList;
        notifyDataSetChanged();
    }

    public class VideoItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView mTrailerName;
        VideoItemViewHolder(View itemView) {
            super(itemView);
            mTrailerName = itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            mItemClickListener.onClick(mTrailerList.get(getAdapterPosition()));
        }

    }

    public interface ListItemClickListener {
        void onClick(TrailerDataItem item);

    }


}

