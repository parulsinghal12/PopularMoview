package com.example.parul.movietheatre;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.parul.movietheatre.Adapter.MovieReviewRecycAdapter;
import com.example.parul.movietheatre.Adapter.MovieTrailerRecycAdapter;
import com.example.parul.movietheatre.Data.MovieAppConstants;
import com.example.parul.movietheatre.Data.MovieDataItem;
import com.example.parul.movietheatre.Data.MovieViewModel;
import com.example.parul.movietheatre.Data.ReviewDataItem;
import com.example.parul.movietheatre.Data.TrailerDataItem;
import com.example.parul.movietheatre.Loader.MovieReviewsJsonLoader;
import com.example.parul.movietheatre.Loader.MovieTrailerJsonLoader;
import com.example.parul.movietheatre.Utilities.JsonParser;
import com.example.parul.movietheatre.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.parul.movietheatre.Data.MovieAppConstants.MOVIE_OBJECT;
import static com.example.parul.movietheatre.Data.MovieAppConstants.MOVIE_REVIEW_ITEM;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDataItem> ,
        MovieTrailerRecycAdapter.ListItemClickListener,
        MovieReviewRecycAdapter.ListItemClickListener{

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    //Create Loader ID
    private static final int LOADER_MOVIE_REVIEW_CALLBACK_ID = 400;
    private static final int LOADER_MOVIE_TRAILER_CALLBACK_ID = 300;
    private static final String LOADER_BUNDLE_MOVIE_KEY = "LOADER_BUNDLE_MOVIE_KEY";
    private static final String LOADER_BUNDLE_TYPE_KEY = "LOADER_BUNDLE_TYPE_KEY";
    private static final String LOADER_BUNDLE_KEY = "LOADER_BUNDLE_KEY";

    private TextView mTitleView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateView;
    private TextView mRatingView;
    private TextView mOverview;
    private ToggleButton mToggleBtn;
    private TextView mTrailerStatus;
    private TextView mReviewStatus;

    private MovieViewModel movieViewModel;

    private RecyclerView mMovieTrailerRecView, mMovieReviewRecView ;
    private MovieTrailerRecycAdapter trailerAdapter;
    private MovieReviewRecycAdapter reviewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mTitleView = (TextView)findViewById(R.id.tv_movie_name);
        mPosterImageView = (ImageView) findViewById(R.id.iv_poster);
        mReleaseDateView = (TextView)findViewById(R.id.tv_release_date);
        mRatingView = (TextView)findViewById(R.id.tv_rating);
        mOverview = (TextView)findViewById(R.id.tv_overview);

        mToggleBtn = (ToggleButton) findViewById(R.id.button_favorite);

        //++ displaying Trailer
        mTrailerStatus = (TextView)findViewById(R.id.tv_trailer_status);
        mMovieTrailerRecView = (RecyclerView)findViewById(R.id.rv_trailer_list);
        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mMovieTrailerRecView.setLayoutManager(trailerLayoutManager);
        mMovieTrailerRecView.addItemDecoration(new DividerItemDecoration(this, trailerLayoutManager.getOrientation()) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                // hide the divider for the last child
                if (position == parent.getAdapter().getItemCount() - 1) {
                    outRect.setEmpty();
                } else {
                    super.getItemOffsets(outRect, view, parent, state);
                }
            }
        });

        trailerAdapter = new MovieTrailerRecycAdapter(this,this);
        mMovieTrailerRecView.setAdapter(trailerAdapter);
        mMovieTrailerRecView.setNestedScrollingEnabled(false);
        //-- displaying Trailer

        //++ displaying Review
        mReviewStatus = (TextView)findViewById(R.id.tv_review_status);
        mMovieReviewRecView = (RecyclerView)findViewById(R.id.rv_reviews_list);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mMovieReviewRecView.setLayoutManager(reviewLayoutManager);
        mMovieReviewRecView.addItemDecoration(new DividerItemDecoration(this, reviewLayoutManager.getOrientation()) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                // hide the divider for the last child
                if (position == parent.getAdapter().getItemCount() - 1) {
                    outRect.setEmpty();
                } else {
                    super.getItemOffsets(outRect, view, parent, state);
                }
            }
        });
        reviewAdapter = new MovieReviewRecycAdapter(this,this);
        mMovieReviewRecView.setAdapter(reviewAdapter);
        mMovieReviewRecView.setNestedScrollingEnabled(false);
        //-- displaying Review

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        MovieDataItem movieData = null;
        if(getIntent()!=null) {

            if (getIntent().hasExtra(MOVIE_OBJECT)) {
                movieData = (MovieDataItem) getIntent().getParcelableExtra(MOVIE_OBJECT);
            }
        }

        if(movieData!=null){
            mTitleView.setText(movieData.getTitle());

            //String posterPath = getIntent().getStringExtra(movieData.getPosterPath());
            String url = NetworkUtils.buildUrlForPoster(movieData.getPosterPath());
            Picasso.with(this).load(url).into(mPosterImageView);

            mReleaseDateView.setText(movieData.getReleaseDate());

            double rating = movieData.getRating();
            mRatingView.append(getString(R.string.user_rating) +
                    " "+ String.valueOf(rating)+"/"+ getString(R.string.highest_rating));
            //mRatingView.setText(String.valueOf(rating));

            mOverview.setText(movieData.getOverview());


            Log.d(TAG,"time start = " + System.currentTimeMillis());
            boolean value = false;
            if(movieData.getFavourite() == 1 || movieViewModel.isMovieIDFav(movieData))
                value = true;
            Log.d(TAG,"time elapsed = " + System.currentTimeMillis());
            mToggleBtn.setChecked(value);
            if(NetworkUtils.isNetworkAvailable(this)){

                loadDataWithTrailerAndReviews(movieData);
            }else{
                Log.d(TAG,"no network dont run loader");
                MovieDataItem movieDataFromDB = movieViewModel.getMovieFromFavList(movieData);
                if(movieDataFromDB != null){
                    updateVideoNTrailerViews(movieDataFromDB);
                }
            }

        }

        final MovieDataItem finalMovieData = movieData;
        mToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(finalMovieData !=null) {
                        finalMovieData.setFavourite(1);
                        movieViewModel.insertItem(finalMovieData);
                    }
                } else {
                    finalMovieData.setFavourite(0);
                    movieViewModel.deleteItem(finalMovieData);
                    // The toggle is disabled
                }
            }
        });


    }


    private void loadDataWithTrailerAndReviews(MovieDataItem movie) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(LOADER_BUNDLE_MOVIE_KEY,movie);

        Loader<MovieDataItem> movieTrailerDetailLoader = getSupportLoaderManager().getLoader(LOADER_MOVIE_TRAILER_CALLBACK_ID);
        if(movieTrailerDetailLoader == null){
            getSupportLoaderManager().initLoader(LOADER_MOVIE_TRAILER_CALLBACK_ID,bundle,this);
        }else{
            getSupportLoaderManager().restartLoader(LOADER_MOVIE_TRAILER_CALLBACK_ID,bundle,this);
        }

        Loader<MovieDataItem> movieReviewDetailLoader = getSupportLoaderManager().getLoader(LOADER_MOVIE_REVIEW_CALLBACK_ID);
        if(movieReviewDetailLoader == null){
            getSupportLoaderManager().initLoader(LOADER_MOVIE_REVIEW_CALLBACK_ID,bundle,this);
        }else{
            getSupportLoaderManager().restartLoader(LOADER_MOVIE_REVIEW_CALLBACK_ID,bundle,this);
        }

    }

    @Override
    public Loader<MovieDataItem> onCreateLoader(int id, final Bundle args) {
        MovieDataItem movieBundle = args.getParcelable(LOADER_BUNDLE_MOVIE_KEY);
        if(id == LOADER_MOVIE_TRAILER_CALLBACK_ID)
            return new MovieTrailerJsonLoader(MovieDetailActivity.this, movieBundle);
        else /*(id == LOADER_MOVIE_REVIEW_CALLBACK_ID)*/
            return new MovieReviewsJsonLoader(MovieDetailActivity.this, movieBundle);
    }

    @Override
    public void onLoadFinished(Loader<MovieDataItem> loader, MovieDataItem data) {
        Log.d(TAG,"onLoadFinished loader id == "+loader.getId());
        if(data != null) {
            Log.d(TAG, "data: trailer == " + data.getTrailerDataItems());
            Log.d(TAG, "data: reviews == " + data.getReviewDataItems());
        }
        updateVideoNTrailerViews(data);
        if(movieViewModel.isMovieIDFav(data))
            movieViewModel.updateItem(data);//update DB only when it is marked fav.
    }

    @Override
    public void onLoaderReset(Loader<MovieDataItem> loader) {

    }

    private void updateVideoNTrailerViews(MovieDataItem data) {
        if(data.getTrailerDataItems()==null || data.getTrailerDataItems().size()<=0){
            mMovieTrailerRecView.setVisibility(View.GONE);
            mTrailerStatus.setVisibility(View.VISIBLE);
        }else{
            mMovieTrailerRecView.setVisibility(View.VISIBLE);
            //String id, String trailerKey, String trailerSite, String trailerName
            ArrayList<TrailerDataItem> trailerData = data.getTrailerDataItems();
            trailerAdapter.setMovieData(trailerData);
            mTrailerStatus.setVisibility(View.GONE);
        }
        if(data.getReviewDataItems()==null || data.getReviewDataItems().size()<=0){
            mMovieReviewRecView.setVisibility(View.GONE);
            mReviewStatus.setVisibility(View.VISIBLE);
            mReviewStatus.setText(R.string.status);
        }else{
            mMovieReviewRecView.setVisibility(View.VISIBLE);
            //String id, String trailerKey, String trailerSite, String trailerName
            ArrayList<ReviewDataItem> reviewData = data.getReviewDataItems();
            reviewAdapter.setMovieData(reviewData);
            mReviewStatus.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(TrailerDataItem item) {
        if(NetworkUtils.isNetworkAvailable(this)){
            //create youtube link.

            String videoKey = item.getTrailerKey();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoKey));

            // Check if the youtube app exists on the device
            if (intent.resolveActivity(getPackageManager()) == null) {
                // If the youtube app doesn't exist, then use the browser
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + videoKey));
            }

            startActivity(intent);
        }else{
            Toast.makeText(this,R.string.network_status,Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onClick(ReviewDataItem item) {
        if(NetworkUtils.isNetworkAvailable(this)){
            String url = item.getUrl();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
        else{
            Intent intent = new Intent(this,MovieReviewActivity.class);
            intent.putExtra(MOVIE_REVIEW_ITEM,item);

            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {

            StringBuilder sb = new StringBuilder()
                    .append(mTitleView.getText())
                    .append("\n")
                    .append(mReleaseDateView.getText())
                    .append("\n")
                    .append(mRatingView.getText())
                    .append("\n")
                    .append(mOverview.getText())
                    .append("\n");



            String mimeType = "text/plain";
            String title = "";
            ShareCompat.IntentBuilder
                    /* The from method specifies the Context from which this share is coming from */
                    .from(this)
                    .setType(mimeType)
                    .setChooserTitle(title)
                    .setText(sb)
                    .startChooser();
                    return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
