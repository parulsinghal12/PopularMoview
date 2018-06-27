package com.example.parul.movietheatre.Loader;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.parul.movietheatre.Data.MovieDataItem;
import com.example.parul.movietheatre.Data.TrailerDataItem;
import com.example.parul.movietheatre.Utilities.JsonParser;
import com.example.parul.movietheatre.Utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MovieTrailerJsonLoader extends
        AsyncTaskLoader<MovieDataItem> {

    ArrayList<TrailerDataItem> jsonResponseTrailerList;
    MovieDataItem movie;
    Context context;

    public MovieTrailerJsonLoader(Context context) {
        super(context);
    }

    public MovieTrailerJsonLoader(Context context,MovieDataItem movie) {
        super(context);
        this.movie = movie;
        this.context = context;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        //mProgressBar.setVisibility(View.VISIBLE);

        if(jsonResponseTrailerList == null || jsonResponseTrailerList.size()<=0)
            forceLoad();
        else{
            deliverResult(movie);
        }
    }

    @Override
    public MovieDataItem loadInBackground() {


        URL TrailerRequestUrl = NetworkUtils.buildUrlForTrailerVideos(String.valueOf(movie.getId()));

        try {
            //++ get Json data for Trailer videos and update in local data list


            String jsonTrailerResponse = NetworkUtils
                    .getResponseFromHttpUrl(TrailerRequestUrl);

            ArrayList<TrailerDataItem> parsedTrailerDataList = JsonParser
                    .getSimpleStringsFromTrailerJson(getContext(), jsonTrailerResponse, movie);

            //TODO : useless as it will pass by reference. for cashing need to check other alternate
            MovieDataItem newMovieData = movie;
            if(parsedTrailerDataList!=null || parsedTrailerDataList.size()>=0) {
                newMovieData.setTrailerData(jsonTrailerResponse);//update Json Data
                newMovieData.setTrailerDataItems(parsedTrailerDataList);

            }

            return newMovieData;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deliverResult(MovieDataItem data) {
        movie = data;
        jsonResponseTrailerList = data.getTrailerDataItems();
        super.deliverResult(data);
    }
}