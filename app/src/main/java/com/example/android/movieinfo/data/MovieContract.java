package com.example.android.movieinfo.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import static android.R.attr.path;

/**
 * Created by Santosh on 21-06-2017.
 */

public class MovieContract  {
    public static final String Authorities="com.example.android.movieinfo";
    public static final Uri BaseUri= Uri.parse("content://"+Authorities);
    public static final String Path="movies";

    private MovieContract(){}

    public static class MovieEntry implements BaseColumns
    {
          public static final Uri ContentUri=BaseUri.buildUpon().appendPath(Path).build();
        public static final String contenttype1 =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Authorities + "/" + Path;
        public static final String contenttype2 =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Authorities + "/" + Path + "/#";
        public static final String Id=BaseColumns._ID;
        public static final String Table_Name="movies";
        public static final String Movie_Name="Name";
        public static final String Movie_Poster_Path="image";
        public static final String Movie_Rating="rating";
        public static final String Release_Date="Date";






    }
}
