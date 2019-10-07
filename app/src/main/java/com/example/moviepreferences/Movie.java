package com.example.moviepreferences;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_table")
public class Movie implements Parcelable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "mid")
    private int mId;
    @ColumnInfo(name = "mtitle")
    private String mTitle;
    @ColumnInfo(name = "mimage")
    private String mImage;
    @ColumnInfo(name = "moverview")
    private String mOverview;
    @ColumnInfo(name = "mrating")
    private double mRating;
    @ColumnInfo(name = "mrdate")
    private String mReleaseDate;
    @ColumnInfo(name = "rList")
    private Reviews review;

    public Movie() {
    }

    public Reviews getReview() {
        return review;
    }

    public void setReview(Reviews review) {
        this.review = review;
    }

    public Movie(int mId, String mTitle, String mImage, String mOverview, double mRating, String mReleaseDate, Reviews reviews) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mImage = mImage;
        this.mOverview = mOverview;
        this.mRating = mRating;
        this.mReleaseDate = mReleaseDate;
        this.review = reviews;
    }

    protected Movie(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mImage = in.readString();
        mOverview = in.readString();
        mRating = in.readDouble();
        mReleaseDate = in.readString();
        review = in.readParcelable(Reviews.class.getClassLoader());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double mRating) {
        this.mRating = mRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mImage);
        dest.writeString(mOverview);
        dest.writeDouble(mRating);
        dest.writeString(mReleaseDate);
        dest.writeParcelable(review, flags);
    }
}
