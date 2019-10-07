package com.example.moviepreferences;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class Reviews implements Parcelable {

    private List<String> mReviews;

    public Reviews(List<String> reviews) {
        mReviews = reviews;
    }

    protected Reviews(Parcel in) {
        mReviews = in.createStringArrayList();
    }

    public static final Creator<Reviews> CREATOR = new Creator<Reviews>() {
        @Override
        public Reviews createFromParcel(Parcel in) {
            return new Reviews(in);
        }

        @Override
        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };

    public List<String> getReviews() {
        return mReviews;
    }

    public void setReviews(List<String> mReviews) {
        this.mReviews = mReviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(mReviews);
    }
}

