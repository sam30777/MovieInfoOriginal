package com.example.android.movieinfo;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieinfo.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Santosh on 08-06-2017.
 */

public class movieAdapter extends ArrayAdapter<movieData> {
    public movieAdapter(Activity context, ArrayList<movieData> movieDatas) {
        super(context, 0, movieDatas);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movielist, parent, false);

        }
        final movieData data = getItem(position);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.movieImage);
        Picasso.with(getContext()).load(data.getImage()).into(imageView);
        TextView textView = (TextView) convertView.findViewById(R.id.ratingText);
        textView.setText(String.valueOf(data.getRating()));

        return convertView;

    }
}
