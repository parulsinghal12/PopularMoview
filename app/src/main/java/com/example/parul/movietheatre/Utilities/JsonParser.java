package com.example.parul.movietheatre.Utilities;

import android.content.Context;
import android.util.Log;

import com.example.parul.movietheatre.Data.MovieDataItem;
import com.example.parul.movietheatre.Data.ReviewDataItem;
import com.example.parul.movietheatre.Data.TrailerDataItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {

    private static final String TAG = JsonParser.class.getSimpleName();


    public static ArrayList<MovieDataItem> getSimpleStringsFromJson(Context context, String jsonStr)
            throws JSONException {

        final String JSON_ID = "id";
        final String JSON_TITLE = "title";
        final String JSON_POSTER_PATH = "poster_path";
        final String JSON_VOTE_AVG = "vote_average";
        final String JSON_OVERVIEW = "overview";
        final String JSON_REL_DATE = "release_date";
        final String JSON_STATUS_CODE = "status_code";


        JSONObject movieJsonContent = new JSONObject(jsonStr);
        if (movieJsonContent.has(JSON_STATUS_CODE)) {
            int errorCode = movieJsonContent.getInt(JSON_STATUS_CODE);

            switch (errorCode) {
                case 7:
                    Log.i(TAG, "error code 7");
                    return null;
                case 34:
                    Log.i(TAG, "error code 34");
                    return null;
                default:
                    Log.i(TAG, "default error");
                    return null;
            }
        }


        JSONArray movieArray = movieJsonContent.getJSONArray("results");
        ArrayList<MovieDataItem> MovieDataList = new ArrayList<>();
        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieObject = movieArray.getJSONObject(i);
            int id;
            String title, overview, poster_path, release_date;
            double voting_avg;
            //if(movieObject.has(JSON_ID)){
            id = (int) movieObject.get(JSON_ID);
            //}
            //if(movieObject.has(JSON_TITLE)){
            title = movieObject.getString(JSON_TITLE);
            //}
            //if(movieObject.has(JSON_OVERVIEW)){
            overview = movieObject.getString(JSON_OVERVIEW);
            //}
            //if(movieObject.has(JSON_POSTER_PATH)){
            poster_path = movieObject.getString(JSON_POSTER_PATH);
            //}
            //if(movieObject.has(JSON_REL_DATE)){
            release_date = movieObject.getString(JSON_REL_DATE);
            //}
            //if(movieObject.has(JSON_VOTE_AVG)){
            voting_avg = ((Number) movieObject.get(JSON_VOTE_AVG)).doubleValue();
            //}


            MovieDataItem movie = new MovieDataItem(id, title, poster_path, overview, voting_avg, release_date);
            MovieDataList.add(movie);
        }

        return MovieDataList;
    }

    public static ArrayList<TrailerDataItem> getSimpleStringsFromTrailerJson(Context context, String jsonStr, MovieDataItem movieDataItem)
            throws JSONException {

        final String JSON_ID = "id";
        final String JSON_KEY = "key"; //append at the of url
        final String JSON_TRAILER_NAME = "name"; // trailer 1
        final String JSON_SITE = "site"; //youtube
        final String JSON_STATUS_CODE = "status_code";


        JSONObject movieJsonContent = new JSONObject(jsonStr);
        if (movieJsonContent.has(JSON_STATUS_CODE)) {
            int errorCode = movieJsonContent.getInt(JSON_STATUS_CODE);

            switch (errorCode) {
                case 7:
                    Log.i(TAG, "error code 7");
                    return null;
                case 34:
                    Log.i(TAG, "error code 34");
                    return null;
                default:
                    Log.i(TAG, "default error");
                    return null;
            }
        }


        JSONArray movieArray = movieJsonContent.getJSONArray("results");
        ArrayList<TrailerDataItem> trailerDataItemList = new ArrayList<>();
        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieObject = movieArray.getJSONObject(i);
            String id;
            String key, name, site;
            if(!movieObject.has(JSON_ID)){
                return null;
            }
            id =  movieObject.getString(JSON_ID);
            //}
            //if(movieObject.has(JSON_KEY)){
            key = movieObject.getString(JSON_KEY);
            //}
           // if(movieObject.has(JSON_SITE)){
            site = movieObject.getString(JSON_SITE);
           // }
           // if(movieObject.has(JSON_TRAILER_NAME)){
                name = movieObject.getString(JSON_TRAILER_NAME);
           // }


            TrailerDataItem trailer = new TrailerDataItem(id, key, site, name);
            trailerDataItemList.add(trailer);
        }


        return trailerDataItemList;
    }

    public static ArrayList<ReviewDataItem> getSimpleStringsFromReviewJson(Context context, String jsonStr, MovieDataItem movieDataItem)
            throws JSONException {

        final String JSON_ID = "id";
        final String JSON_CONTENT = "content";
        final String JSON_AUTHOR = "author";
        final String JSON_URL = "url";
        final String JSON_STATUS_CODE = "status_code";


        JSONObject movieJsonContent = new JSONObject(jsonStr);
        if (movieJsonContent.has(JSON_STATUS_CODE)) {
            int errorCode = movieJsonContent.getInt(JSON_STATUS_CODE);

            switch (errorCode) {
                case 7:
                    Log.i(TAG, "error code 7");
                    return null;
                case 34:
                    Log.i(TAG, "error code 34");
                    return null;
                default:
                    Log.i(TAG, "default error");
                    return null;
            }
        }


        JSONArray movieArray = movieJsonContent.getJSONArray("results");
        ArrayList<ReviewDataItem> reviewDataItemList = new ArrayList<>();
        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieObject = movieArray.getJSONObject(i);
            String id;
            String content, author, url;
            if(!movieObject.has(JSON_ID))
                return null;

            id =  movieObject.getString(JSON_ID);
            //}
            //if(movieObject.has(JSON_KEY)){
            content = movieObject.getString(JSON_CONTENT);
            //}
            //if(movieObject.has(JSON_TRAILER_NAME)){
            author = movieObject.getString(JSON_AUTHOR);
            //}
            //if(movieObject.has(JSON_SITE)){
            url = movieObject.getString(JSON_URL);
            //}


            ReviewDataItem ReviewDataItem = new ReviewDataItem(id, author, url,content);
            reviewDataItemList.add(ReviewDataItem);
        }


        return reviewDataItemList;
    }
}
