package com.example.android.movieinfo;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.example.android.movieinfo.data.MovieContract;

public class Favourate extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    MovieCursorAdapter movieCursorAdapter;
    GridView gridView;
    private int loaderId = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourate);
        movieCursorAdapter = new MovieCursorAdapter(this, null);
        gridView = (GridView) findViewById(R.id.fGrid);
        getLoaderManager().initLoader(loaderId, null, this);
        loaderId++;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        movieCursorAdapter.swapCursor(cursor);
        gridView.setAdapter(movieCursorAdapter);


    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieCursorAdapter.swapCursor(null);


    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.Movie_Poster_Path,
                MovieContract.MovieEntry.Movie_Rating


        };
        return new CursorLoader(this, MovieContract.MovieEntry.ContentUri, projection, null, null, null);
    }
}
