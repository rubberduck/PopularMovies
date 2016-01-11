package com.rubberduck.udacity.popularmovies.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;


public class MovieParcel implements Parcelable {

    private long id;
    private String originalTitle;
    private String releaseDate;
    private boolean adultMovie;
    private String posterPath;
    private String synopsis;
    private double voteAverage;
    private long revenue;
    private long budget;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isAdultMovie() {
        return adultMovie;
    }

    public void setAdultMovie(boolean adultMovie) {
        this.adultMovie = adultMovie;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public MovieParcel(Parcel inputParcel) {
        this.id = inputParcel.readLong();
        this.originalTitle = inputParcel.readString();
        this.releaseDate = inputParcel.readString();
        this.adultMovie = inputParcel.readByte() != 0x00;
        this.posterPath = inputParcel.readString();
        this.synopsis = inputParcel.readString();
        this.voteAverage = inputParcel.readDouble();
        this.revenue = inputParcel.readLong();
    }

    public MovieParcel() {

    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(this.originalTitle);
        dest.writeString(this.releaseDate);
        dest.writeByte((byte) (this.adultMovie ? 0x01 : 0x00));
        dest.writeString(this.posterPath);
        dest.writeString(this.synopsis);
        dest.writeDouble(this.voteAverage);
        dest.writeLong(this.revenue);
    }


    public static final Parcelable.Creator<MovieParcel> CREATOR = new Parcelable.Creator<MovieParcel>() {
        @Override
        public MovieParcel createFromParcel(Parcel inputParcel) {
            return new MovieParcel(inputParcel);
        }

        @Override
        public MovieParcel[] newArray(int arraySize) {
            return new MovieParcel[arraySize];
        }
    };

}

