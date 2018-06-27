package com.example.parul.movietheatre.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.example.parul.movietheatre.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String AUTHORITY_FOR_MOVIE_DB = "api.themoviedb.org";

    private static final String api_key = BuildConfig.API_KEY;
    private static final String language = "en-US";
    private static final String page_count = "1";

    private static final String BASE_URL_FOR_POSTER = "https://image.tmdb.org/t/p";
    private static final String poster_image_size = "w500";

    private final static String API_PARAM = "api_key";
    private final static String PAGE_PARAM = "page";
    private final static String LANGUAGE_PARAM = "language";


    //https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&language=en-US&page=1
    public static URL buildUrl(String sortBy) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(AUTHORITY_FOR_MOVIE_DB)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(sortBy)
                .appendQueryParameter(API_PARAM, api_key)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .appendQueryParameter(PAGE_PARAM, page_count);

        URL url = null;
        try {
            url = new URL(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String buildUrlForPoster(String posterPath) {
        Uri builtUri = Uri.parse(BASE_URL_FOR_POSTER).buildUpon()
                .appendEncodedPath(poster_image_size)
                .appendEncodedPath(posterPath)
                .build();

        String url = builtUri.toString();

        Log.i(TAG,"url for poster == " + url);

        return url;
    }

    //https://api.themoviedb.org/3/movie/{movie_id}/reviews?api_key=<<api_key>>&language=en-US&page=1
    public static URL buildUrlForReviewComments(String movie_id) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(AUTHORITY_FOR_MOVIE_DB)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movie_id)
                .appendPath("reviews")
                .appendQueryParameter(API_PARAM, api_key)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .appendQueryParameter(PAGE_PARAM, page_count);

        URL url = null;
        try {
            url = new URL(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    //https://api.themoviedb.org/3/movie/{movie_id}/videos?api_key=<<api_key>>&language=en-US
    public static URL buildUrlForTrailerVideos(String movieId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(AUTHORITY_FOR_MOVIE_DB)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieId)
                .appendPath("videos")
                .appendQueryParameter(API_PARAM, api_key)
                .appendQueryParameter(LANGUAGE_PARAM, language);

        URL url = null;
        try {
            url = new URL(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {

            urlConnection.setRequestMethod("GET");
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
