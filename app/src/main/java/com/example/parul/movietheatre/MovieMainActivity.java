package com.example.parul.movietheatre;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.parul.movietheatre.Adapter.MovieRecyclerAdapter;
import com.example.parul.movietheatre.Data.MovieAppConstants;
import com.example.parul.movietheatre.Data.MovieDataItem;
import com.example.parul.movietheatre.Data.MovieViewModel;
import com.example.parul.movietheatre.Utilities.JsonParser;
import com.example.parul.movietheatre.Utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.parul.movietheatre.Data.MovieAppConstants.MOVIE_OBJECT;
import static com.example.parul.movietheatre.Data.MovieAppConstants.SORT_BY_FAVOURITES;
import static com.example.parul.movietheatre.Data.MovieAppConstants.SORT_BY_POPULARITY;
import static com.example.parul.movietheatre.Data.MovieAppConstants.SORT_BY_TOPRATED;

public class MovieMainActivity extends AppCompatActivity implements MovieRecyclerAdapter.ListItemClickListener
        , LoaderManager.LoaderCallbacks<ArrayList<MovieDataItem>>{

    private RecyclerView mMovieRecView ;
    private MovieRecyclerAdapter adapter;
    private TextView mErrorTextView;
    private ProgressBar mProgressBar;
    private final int numOfColPortrait = 2;
    private final int numOfColLandscape = 4;
    private static String menuSelectedType = SORT_BY_POPULARITY;
    private static final String SORTING_TYPE_KEY = "SORTING_TYPE_KEY";
    private final int CHECK_MOVIE_FAV = 101;

    //Create Loader ID
    private static final int LOADER_MOVIE_CALLBACK_ID_POPULARITY = 100;
    private static final int LOADER_MOVIE_CALLBACK_ID_RATING = 100;
    private static final String LOADER_BUNDLE_KEY = "LOADER_BUNDLE_KEY";

    private static final String BUNDLE_RECYCLER_STATE = "BUNDLE_RECYCLER_STATE";
    Parcelable savedRecyclerLayoutState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_discovery);
        mErrorTextView = (TextView)findViewById(R.id.tv_error_msg);
        mProgressBar = (ProgressBar)findViewById(R.id.pb_loading);
        mMovieRecView = (RecyclerView)findViewById(R.id.rv_movielist);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            mMovieRecView.setLayoutManager(new GridLayoutManager(this,numOfColPortrait));
        else
            mMovieRecView.setLayoutManager(new GridLayoutManager(this,numOfColLandscape));
        adapter = new MovieRecyclerAdapter(this,this);
        mMovieRecView.setAdapter(adapter);
        //set adapter
        if(savedInstanceState != null && savedInstanceState.containsKey(SORTING_TYPE_KEY)){
            menuSelectedType = savedInstanceState.getString(SORTING_TYPE_KEY);
        }
        if(menuSelectedType.equals(SORT_BY_POPULARITY))
            loadMovieDataByPopularity();
        else if(menuSelectedType.equals(SORT_BY_TOPRATED))
            loadMovieDataByRating();
        else
            loadMovieDataByFav();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORTING_TYPE_KEY,menuSelectedType);

        //maintaining scroll state
        outState.putParcelable(BUNDLE_RECYCLER_STATE, mMovieRecView.getLayoutManager().onSaveInstanceState());

    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null)
        {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_STATE);
            mMovieRecView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }else{
            savedRecyclerLayoutState = null;
        }
    }



    private void loadMovieDataByPopularity() {
        setTitle(R.string.app_name);
        mErrorTextView.setVisibility(View.GONE);
        mMovieRecView.setVisibility(View.VISIBLE);
        /*new FetchMovieTask().execute(SORT_BY_POPULARITY);*/
        Bundle bundle = new Bundle();
        bundle.putString(LOADER_BUNDLE_KEY,MovieAppConstants.SORT_BY_POPULARITY);

        Loader<ArrayList<MovieDataItem>> movieDetailLoader = getSupportLoaderManager().getLoader(LOADER_MOVIE_CALLBACK_ID_POPULARITY);
        if(movieDetailLoader == null){
            getSupportLoaderManager().initLoader(LOADER_MOVIE_CALLBACK_ID_POPULARITY,bundle,this);
        }else{
            getSupportLoaderManager().restartLoader(LOADER_MOVIE_CALLBACK_ID_POPULARITY,bundle,this);
        }
    }

    private void loadMovieDataByRating() {
        setTitle(R.string.top_rated_movies);
        mErrorTextView.setVisibility(View.GONE);
        mMovieRecView.setVisibility(View.VISIBLE);
        //new FetchMovieTask().execute(MovieAppConstants.SORT_BY_TOPRATED);
        Bundle bundle = new Bundle();
        bundle.putString(LOADER_BUNDLE_KEY,MovieAppConstants.SORT_BY_TOPRATED);

        Loader<ArrayList<MovieDataItem>> movieDetailLoader = getSupportLoaderManager().getLoader(LOADER_MOVIE_CALLBACK_ID_RATING);
        if(movieDetailLoader == null){
            getSupportLoaderManager().initLoader(LOADER_MOVIE_CALLBACK_ID_RATING,bundle,this);
        }else{
            getSupportLoaderManager().restartLoader(LOADER_MOVIE_CALLBACK_ID_RATING,bundle,this);
        }
    }

    private void loadMovieDataByFav() {
        setTitle(R.string.favourite_movies);
        mErrorTextView.setVisibility(View.GONE);
        mMovieRecView.setVisibility(View.VISIBLE);

        MovieViewModel viewModel = new MovieViewModel(getApplication());
        //LiveData<ArrayList<MovieDataItem>> movieData  = viewModel.getAllFavMovieList();

        viewModel.getAllFavMovieList().observe(this, new Observer<List<MovieDataItem>>() {
            @Override
            public void onChanged(@Nullable final List<MovieDataItem> movieList) {
                // Update the cached copy of the movies in the adapter.
                ArrayList<MovieDataItem> movieArrayList = new ArrayList<MovieDataItem>(movieList);
                if(movieArrayList == null || movieArrayList.size()<=0){
                    mErrorTextView.setVisibility(View.VISIBLE);
                    mErrorTextView.setText(R.string.no_fav_found_msg);
                    mMovieRecView.setVisibility(View.GONE);
                }else{
                    mErrorTextView.setVisibility(View.GONE);
                    mMovieRecView.setVisibility(View.VISIBLE);
                    adapter.setMovieData(movieArrayList);
                }

            }
        });



    }

    @Override
    public void onClick(MovieDataItem movieDetail) {
        Intent intent = new Intent(this,MovieDetailActivity.class);
        intent.putExtra(MOVIE_OBJECT,movieDetail);

        startActivity(intent);

    }

    @Override
    public Loader<ArrayList<MovieDataItem>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<MovieDataItem>>(this) {
            ArrayList<MovieDataItem> movieDataListResultForPopularity;
            ArrayList<MovieDataItem> movieDataListResultForRating;
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null || !args.containsKey(LOADER_BUNDLE_KEY))
                    return;
                String sortBy = args.getString(LOADER_BUNDLE_KEY);

                switch (sortBy) {

                    case MovieAppConstants.SORT_BY_POPULARITY:
                        if(movieDataListResultForPopularity!=null)
                            deliverResult(movieDataListResultForPopularity);
                        else {
                            mProgressBar.setVisibility(View.VISIBLE);
                            forceLoad();
                        }
                        break;

                    case MovieAppConstants.SORT_BY_TOPRATED:
                        if(movieDataListResultForRating!=null)
                            deliverResult(movieDataListResultForRating);
                        else {
                            mProgressBar.setVisibility(View.VISIBLE);
                            forceLoad();
                        }
                        break;
                }

            }

            @Override
            public ArrayList<MovieDataItem> loadInBackground() {

                String sortBy = args.getString(LOADER_BUNDLE_KEY);

                switch (sortBy){
                    case MovieAppConstants.SORT_BY_TOPRATED:
                    case MovieAppConstants.SORT_BY_POPULARITY:
                        URL moviewRequestUrl = NetworkUtils.buildUrl(sortBy);

                        try {
                            String jsonMovieResponse = NetworkUtils
                                    .getResponseFromHttpUrl(moviewRequestUrl);

                            ArrayList<MovieDataItem> movieData = JsonParser
                                    .getSimpleStringsFromJson(MovieMainActivity.this, jsonMovieResponse);

                            /*for(MovieDataItem movie : movieData){
                                boolean value = isMovieMarkedFav(movie);
                                if(value)
                                    movie.setFavourite(1);
                            }*/

                            return movieData;

                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }

                        default:
                            return null;

                }



            }

            @Override
            public void deliverResult(ArrayList<MovieDataItem> data) {
                String sortBy = args.getString(LOADER_BUNDLE_KEY);
                switch (sortBy) {
                    case MovieAppConstants.SORT_BY_TOPRATED:
                        movieDataListResultForRating = data;
                        break;
                    case MovieAppConstants.SORT_BY_POPULARITY:
                        movieDataListResultForPopularity = data;
                        break;
                }

                super.deliverResult(data);
            }

            //return null;
        };

    }


    @Override
    public void onLoadFinished(Loader<ArrayList<MovieDataItem>> loader, ArrayList<MovieDataItem> moviewData) {
        mProgressBar.setVisibility(View.GONE);
        if (moviewData != null && moviewData.size()>0) {
            mErrorTextView.setVisibility(View.GONE);
            mMovieRecView.setVisibility(View.VISIBLE);
            adapter.setMovieData(moviewData);

            if(savedRecyclerLayoutState != null)
            {
                mMovieRecView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            }

        } else {
            mMovieRecView.setVisibility(View.GONE);
            mErrorTextView.setVisibility(View.VISIBLE);
            mErrorTextView.setText(R.string.error_msg);

        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieDataItem>> loader) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sortby_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item_sortby_pop) {
            adapter.setMovieData(null);
            loadMovieDataByPopularity();
            menuSelectedType = SORT_BY_POPULARITY;
            return true;
        }else if (id == R.id.item_sortby_rating) {
            adapter.setMovieData(null);
            loadMovieDataByRating();
            menuSelectedType = SORT_BY_TOPRATED;
            return true;
        }else if (id == R.id.item_sortby_favourites) {
            adapter.setMovieData(null);
            loadMovieDataByFav();
            menuSelectedType = SORT_BY_FAVOURITES;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isMovieMarkedFav(MovieDataItem movie){

        MovieViewModel movieViewModel;
        movieViewModel = ViewModelProviders.of(MovieMainActivity.this).get(MovieViewModel.class);
        return movieViewModel.isMovieIDFav(movie);
    }
}
