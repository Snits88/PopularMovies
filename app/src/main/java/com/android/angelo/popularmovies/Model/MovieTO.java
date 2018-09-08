package com.android.angelo.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

public class MovieTO implements Parcelable{
    private int vote_count;
    private int id;
    private boolean video;
    private double vote_avarage;
    private String title;
    private double popularity;
    private String poster_path;
    private String original_language;
    private String original_title;
    private List<Integer> genre_ids;
    private String backdrop_path;
    private boolean adult;
    private String overview;
    private Date release_date;

    public MovieTO(){ }

    public MovieTO(Parcel parcel){
        this.vote_count  = parcel.readInt();
        this.id = parcel.readInt();
        this.video = parcel.readByte() != 0;
        this.vote_avarage = parcel.readDouble();
        this.title = parcel.readString();
        this.popularity = parcel.readDouble();
        this.poster_path = parcel.readString();
        this.original_language = parcel.readString();
        this.original_title = parcel.readString();
        this.genre_ids = parcel.readArrayList(List.class.getClassLoader());
        this.backdrop_path = parcel.readString();
        this.adult = parcel.readByte() != 0;
        this.overview = parcel.readString();
        this.release_date = (java.util.Date) parcel.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(vote_count);
        dest.writeInt(id);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeDouble(vote_avarage);
        dest.writeString(title);
        dest.writeDouble(popularity);
        dest.writeString(poster_path);
        dest.writeString(original_language);
        dest.writeString(original_title);
        dest.writeList(genre_ids);
        dest.writeString(backdrop_path);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(overview);
        dest.writeSerializable(release_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MovieTO> CREATOR
            = new Parcelable.Creator<MovieTO>() {
        public MovieTO createFromParcel(Parcel in) {
            return new MovieTO(in);
        }

        public MovieTO[] newArray(int size) {
            return new MovieTO[size];
        }
    };


    /** Getters And Setters Method**/

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public Double getVote_avarage() {
        return vote_avarage;
    }

    public void setVote_avarage(double vote_avarage) {
        this.vote_avarage = vote_avarage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

}
