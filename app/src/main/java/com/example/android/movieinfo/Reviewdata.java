package com.example.android.movieinfo;

/**
 * Created by Santosh on 18-06-2017.
 */

public class Reviewdata {
    private String author;
    private String review;
    private String url;

    Reviewdata(String x, String y, String z) {
        author = x;
        review = y;
        url = z;
    }

    public String getAuthor() {
        return author;
    }

    public String getReview() {
        return review;
    }

    public String getUrl() {
        return url;
    }

}
