package com.example.android.movieinfo;

/**
 * Created by Santosh on 08-06-2017.
 */

public class movieData {
    private String title;
    private String movieDesc;
    private String date;
    private String image;
    private Double rating;
    private int mid;
    private String trailerString;


    movieData(String x, String y, String z, double a, String im, int m) {
        title = x;
        movieDesc = y;
        date = z;
        rating = a;
        image = im;
        mid = m;

    }

    public int getMid() {
        return mid;
    }

    public String getTrailerString() {
        return trailerString;
    }

    public String getTitle() {
        return title;
    }

    public String getMovieDesc() {
        return movieDesc;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public double getRating() {
        return rating;
    }
}
