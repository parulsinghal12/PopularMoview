package com.example.parul.movietheatre.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewDataItem implements Parcelable{

    String id;

    String content;

    String author;

    String url;


    public ReviewDataItem(String id, String author, String url, String content) {
        this.id = id;
        this.author = author;
        this.url = url;
        this.content = content;

    }

    public ReviewDataItem(Parcel in) {
        id = in.readString();
        author = in.readString();
        url = in.readString();
        content = in.readString();


    }

    public static final Creator<ReviewDataItem> CREATOR = new Creator<ReviewDataItem>() {
        @Override
        public ReviewDataItem createFromParcel(Parcel in) {
            return new ReviewDataItem(in);
        }

        @Override
        public ReviewDataItem[] newArray(int size) {
            return new ReviewDataItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id);
        out.writeString(author);
        out.writeString(url);
        out.writeString(content);

    }


    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }



}

