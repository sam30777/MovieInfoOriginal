package com.example.android.movieinfo.data;
import com.example.android.movieinfo.data.MovieContract.MovieEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Santosh on 21-06-2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String dataBaseName="movies.db";
    private static  final int dbVersion=1;
    private String SqliteStatement="CREATE TABLE " + MovieEntry.Table_Name + "("
            + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MovieEntry.Movie_Name + " TEXT NOT NULL, "
            + MovieEntry.Movie_Poster_Path + " TEXT NOT NULL, "
            + MovieEntry.Movie_Rating + " REAL, "
            + MovieEntry.Release_Date + " Text);";



  public   MovieDbHelper(Context context)
   {
       super(context,dataBaseName,null,dbVersion);
   }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
             sqLiteDatabase.execSQL(SqliteStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.Table_Name);
        onCreate(sqLiteDatabase);


    }
}
