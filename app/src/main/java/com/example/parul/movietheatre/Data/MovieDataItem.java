package com.example.parul.movietheatre.Data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

@Entity(tableName = "movie")
public class MovieDataItem implements Parcelable{

    @PrimaryKey
    int id;

    @ColumnInfo(name = "MOVIE_TITLE")
    String title;

    @ColumnInfo(name = "POSTER_PATH")
    String posterPath;

    @ColumnInfo(name = "OVERVIEW")
    String overview;

    @ColumnInfo(name = "VOTE_AVG")
    double rating;

    @ColumnInfo(name = "RELEASE_DATE")
    String releaseDate;

    @ColumnInfo(name = "FAVOURITE")
    private int favourite; // 0 = false ; 1 = true

    @ColumnInfo(name = "TRAILER_DATA")
    private String trailerData;

    @ColumnInfo(name = "REVIEW_DATA")
    private String reviewData;

    @Ignore
    private ArrayList<ReviewDataItem> reviewDataItems;

    @Ignore
    private ArrayList<TrailerDataItem> trailerDataItems;




    public MovieDataItem(int id, String title, String posterPath, String overview, double rating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.favourite = 0;
        this.trailerData = null;
        this.reviewData = null;

    }

    public MovieDataItem(Parcel in) {
        id = in.readInt();
        title = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();
        favourite = in.readInt();
        trailerData = in.readString();
        reviewData = in.readString();

    }

    public static final Creator<MovieDataItem> CREATOR = new Creator<MovieDataItem>() {
        @Override
        public MovieDataItem createFromParcel(Parcel in) {
            return new MovieDataItem(in);
        }

        @Override
        public MovieDataItem[] newArray(int size) {
            return new MovieDataItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(id);
        out.writeString(title);
        out.writeString(posterPath);
        out.writeString(overview);
        out.writeDouble(rating);
        out.writeString(releaseDate);
        out.writeInt(favourite);
        out.writeString(trailerData);
        out.writeString(reviewData);


    }



    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getFavourite() {
        return favourite;
    }


    public void setId(int server_movie_id) {
        id = server_movie_id;
    }


    public void setFavourite(int value) {
        favourite = value;
    }

    public String getTrailerData() {
        return trailerData;
    }

    public void setTrailerData(String trailerData) {
        this.trailerData = trailerData;
    }

    public String getReviewData() {
        return reviewData;
    }

    public void setReviewData(String reviewData) {
        this.reviewData = reviewData;
    }

    //local storage for listview
    public ArrayList<ReviewDataItem> getReviewDataItems() {
        return reviewDataItems;
    }

    public void setReviewDataItems(ArrayList<ReviewDataItem> reviewDataItems) {
        this.reviewDataItems = reviewDataItems;
    }

    public ArrayList<TrailerDataItem> getTrailerDataItems() {
        return trailerDataItems;
    }

    public void setTrailerDataItems(ArrayList<TrailerDataItem> trailerDataItems) {
        this.trailerDataItems = trailerDataItems;
    }


}
