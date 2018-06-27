package com.example.parul.movietheatre.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class TrailerDataItem implements Parcelable {

    String id;

    String trailerKey;

    String trailerSite;

    String trailerName;


    public TrailerDataItem(String id, String trailerKey, String trailerSite, String trailerName) {
        this.id = id;
        this.trailerKey = trailerKey;
        this.trailerSite = trailerSite;
        this.trailerName = trailerName;

    }

    public TrailerDataItem(Parcel in) {
        id = in.readString();
        trailerKey = in.readString();
        trailerSite = in.readString();
        trailerName = in.readString();


    }

    public static final Creator<TrailerDataItem> CREATOR = new Creator<TrailerDataItem>() {
        @Override
        public TrailerDataItem createFromParcel(Parcel in) {
            return new TrailerDataItem(in);
        }

        @Override
        public TrailerDataItem[] newArray(int size) {
            return new TrailerDataItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id);
        out.writeString(trailerKey);
        out.writeString(trailerSite);
        out.writeString(trailerName);

    }


    public String getTrailerKey() {
        return trailerKey;
    }

    public String getTrailerSite() {
        return trailerSite;
    }

    public String getTrailerName() {
        return trailerName;
    }



}

