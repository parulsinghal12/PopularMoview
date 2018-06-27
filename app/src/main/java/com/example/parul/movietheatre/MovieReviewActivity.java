package com.example.parul.movietheatre;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.parul.movietheatre.Data.ReviewDataItem;

import static com.example.parul.movietheatre.Data.MovieAppConstants.MOVIE_REVIEW_ITEM;

public class MovieReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView mReviewTextView = (TextView) findViewById(R.id.tv_detailed_review);

        ReviewDataItem reviewDataItem = null;
        if(getIntent()!=null) {

            if (getIntent().hasExtra(MOVIE_REVIEW_ITEM)) {
                reviewDataItem = (ReviewDataItem) getIntent().getParcelableExtra(MOVIE_REVIEW_ITEM);

            }
        }

        if(reviewDataItem!=null) {
            mReviewTextView.setText(reviewDataItem.getContent());
        }
    }
}
