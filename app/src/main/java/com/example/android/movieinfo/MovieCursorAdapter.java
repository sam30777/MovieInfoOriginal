package com.example.android.movieinfo;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieinfo.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by Santosh on 23-06-2017.
 */

public class MovieCursorAdapter extends CursorAdapter {

    public MovieCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.movielist, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        final ImageView imageView = (ImageView) view.findViewById(R.id.movieImage);
        Picasso.with(context).load(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.Movie_Poster_Path))).into(imageView);
        final ImageView imageView1 = (ImageView) view.findViewById(R.id.deletData);
        imageView1.setVisibility(View.VISIBLE);
        final TextView textView = (TextView) view.findViewById(R.id.ratingText);
        textView.setText(String.valueOf(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.Movie_Rating))));


        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry._ID));
                Uri uri = ContentUris.withAppendedId(MovieContract.MovieEntry.ContentUri, id);
                long idDeleted = context.getContentResolver().delete(uri, null, null);

                if (idDeleted > 0) {
                    Toast.makeText(context, "Data deleted", Toast.LENGTH_SHORT).show();


                }
            }
        });

    }
}
