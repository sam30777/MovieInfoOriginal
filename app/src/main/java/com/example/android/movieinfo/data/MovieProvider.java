package com.example.android.movieinfo.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.IllegalFormatException;

/**
 * Created by Santosh on 21-06-2017.
 */

public class MovieProvider extends ContentProvider {
    public static final String LOG_TAG = MovieProvider.class.getSimpleName();
    private static final int All_Data=100;
    private static final int Single_Data=101;
    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    private SQLiteDatabase sqLiteDatabase;
    MovieDbHelper movieDbHelper;
    static {
        uriMatcher.addURI(MovieContract.Authorities,MovieContract.Path,100);
        uriMatcher.addURI(MovieContract.Authorities,MovieContract.Path+"/#",101);
    }
    @Override
    public boolean onCreate() {
        movieDbHelper=new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor;
        sqLiteDatabase=movieDbHelper.getReadableDatabase();

        int id=uriMatcher.match(uri);
        switch (id)
        {
            case All_Data:
                cursor=sqLiteDatabase.query(MovieContract.MovieEntry.Table_Name,null
                ,null,null,null,null,null,null);
                break;
            case Single_Data:
                s= MovieContract.MovieEntry._ID+"=?";
                strings1=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=sqLiteDatabase.query(MovieContract.MovieEntry.Table_Name,null,s,strings1,null,null,null,null);
                break;
                default:
                    throw  new IllegalArgumentException("error in query "+uri);



        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;

    }




    @Nullable
    @Override
    public String getType(Uri uri) {

        int match = uriMatcher.match(uri);
        switch (match) {
            case All_Data:
                return MovieContract.MovieEntry.contenttype1;
            case Single_Data:
                return MovieContract.MovieEntry.contenttype2;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }



    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int idMatch=uriMatcher.match(uri);
        int id;
        sqLiteDatabase=movieDbHelper.getWritableDatabase();
        switch (idMatch)
        {
            case All_Data:
            id=sqLiteDatabase.update(MovieContract.MovieEntry.Table_Name,contentValues,null,null);
                break;


            case Single_Data:
                s= MovieContract.MovieEntry._ID+"=?";
                strings= new String[]{String.valueOf(ContentUris.parseId(uri))};
                id=sqLiteDatabase.update(MovieContract.MovieEntry.Table_Name,contentValues,s,strings);
                break;
                default:
                    throw new IllegalArgumentException("error updating data"+uri);

        }
        if(id==-1)
        {
            Log.e(LOG_TAG, "update:error negative value" );
        }

            return id;


    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        Uri returned=null;
        sqLiteDatabase=movieDbHelper.getWritableDatabase();
       long id= sqLiteDatabase.insert(MovieContract.MovieEntry.Table_Name,null,contentValues);
        if(id==-1)
        {
            Log.e(LOG_TAG, "insert: " );
        }
        else
        { getContext().getContentResolver().notifyChange(uri,null);
        returned= ContentUris.withAppendedId(uri,id);

        }

return returned;

    }


    @Override
    public int delete(Uri uri, String s, String[] strings)
    {
        int id;
        sqLiteDatabase=movieDbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri))
        {
            case All_Data:
                id=sqLiteDatabase.delete(MovieContract.MovieEntry.Table_Name,null,null);
                if(id>0)
                {
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                break;
            case Single_Data:
                s= MovieContract.MovieEntry._ID+"=?";
                strings=new String[]{String.valueOf(ContentUris.parseId(uri))};
                id=sqLiteDatabase.delete(MovieContract.MovieEntry.Table_Name,s,strings);
                if(id>0)
                {
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                break;
            default:
                throw new IllegalArgumentException("error deleting data"+uri);


        }

        return id;
    }
}
