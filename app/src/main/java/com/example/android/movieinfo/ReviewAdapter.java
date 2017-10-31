package com.example.android.movieinfo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Santosh on 18-06-2017.
 */

public class ReviewAdapter extends ArrayAdapter<Reviewdata> {
    ReviewAdapter(Context context, ArrayList<Reviewdata> arrayList) {
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reviewlistitem, parent, false);
        }
        Reviewdata reviewdata = getItem(position);
        TextView textView = (TextView) convertView.findViewById(R.id.authorId);
        textView.setText(reviewdata.getAuthor());
        TextView textView1 = (TextView) convertView.findViewById(R.id.reviewId);
        textView1.setText(reviewdata.getReview());
        textView1.setMaxLines(10);

        return convertView;
    }
}
