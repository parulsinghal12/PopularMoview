package com.example.parul.movietheatre.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.parul.movietheatre.Data.MovieDataItem;
import com.example.parul.movietheatre.R;
import com.example.parul.movietheatre.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieItemViewHolder>{

    private static final String TAG = MovieRecyclerAdapter.class.getSimpleName();
    private ArrayList<MovieDataItem> moviesList = new ArrayList<>();
    private final Context mCtx;
    private final ListItemClickListener mItemClickListener;


    public MovieRecyclerAdapter(Context ctx, ListItemClickListener listener){
        mCtx = ctx;
        mItemClickListener = listener;
    }

    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean shouldAttachToParentImmediately = false;
        View view = LayoutInflater.from(mCtx).inflate(R.layout.movie_list_item,parent,shouldAttachToParentImmediately);
        return new MovieItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieItemViewHolder holder, int position) {
        //ctx = get

        String url = NetworkUtils.buildUrlForPoster(moviesList.get(position).getPosterPath());
        Picasso.with(mCtx).load(url).into(holder.mMovieView);
        //holder.mMovieView.setImageDrawable(MoviesList.get(position).posterPath);
    }

    @Override
    public int getItemCount() {
        if (moviesList== null || moviesList.size() == 0) return 0;
        return moviesList.size();
    }

    public void setMovieData(ArrayList<MovieDataItem> movieDataList){
        moviesList = movieDataList;
        notifyDataSetChanged();
    }

    public class MovieItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final ImageView mMovieView;
        MovieItemViewHolder(View itemView) {
            super(itemView);
            mMovieView = itemView.findViewById(R.id.iv_movie_poster);
            mMovieView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            mItemClickListener.onClick(moviesList.get(getAdapterPosition()));
        }

    }

    public interface ListItemClickListener {
        void onClick(MovieDataItem item);

    }


}
