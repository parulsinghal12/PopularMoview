package com.example.parul.movietheatre.Loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.parul.movietheatre.Data.MovieDataItem;
import com.example.parul.movietheatre.Data.ReviewDataItem;
import com.example.parul.movietheatre.Utilities.JsonParser;
import com.example.parul.movietheatre.Utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MovieReviewsJsonLoader extends
        AsyncTaskLoader<MovieDataItem> {

    ArrayList<ReviewDataItem> jsonResponseReviewList;
    MovieDataItem movie;
    Context context;

    public MovieReviewsJsonLoader(Context context) {
        super(context);
    }

    public MovieReviewsJsonLoader(Context context,MovieDataItem movie) {
        super(context);
        this.movie = movie;
        this.context = context;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        //mProgressBar.setVisibility(View.VISIBLE);

        if(jsonResponseReviewList == null || jsonResponseReviewList.size()<=0)
            forceLoad();
        else{
            deliverResult(movie);
        }
    }

    @Override
    public MovieDataItem loadInBackground() {


        URL ReviewRequestUrl = NetworkUtils.buildUrlForReviewComments(String.valueOf(movie.getId()));

        try {
            //++ get Json data for Trailer videos and update in local data list


            String jsonReviewResponse = NetworkUtils
                    .getResponseFromHttpUrl(ReviewRequestUrl);

            ArrayList<ReviewDataItem> parsedReviewDataList = JsonParser
                    .getSimpleStringsFromReviewJson(getContext(), jsonReviewResponse, movie);

            MovieDataItem newMovieData = movie;
            if(parsedReviewDataList!=null || parsedReviewDataList.size()>=0) {
                newMovieData.setReviewData(jsonReviewResponse);//update Json Data
                newMovieData.setReviewDataItems(parsedReviewDataList);

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
        jsonResponseReviewList = data.getReviewDataItems();
        super.deliverResult(data);
    }
}