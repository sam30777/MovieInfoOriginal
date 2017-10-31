package com.example.android.movieinfo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieinfo.data.MovieContract;
import com.example.android.movieinfo.data.MovieDbHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class DetailedList extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    TextView noTrailer;
    ImageView imageThumb, icon;
     ImageView star;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_list);
        imageThumb = (ImageView) findViewById(R.id.tBackground);
        icon = (ImageView) findViewById(R.id.tYicon);
        progressBar = (ProgressBar) findViewById(R.id.pBar);


        noTrailer = (TextView) findViewById(R.id.blanckTrailer);


        Intent intent = getIntent();


        final String imagePoster = intent.getStringExtra("poster");
        final String movieName = intent.getStringExtra("title");
        final String rDate = intent.getStringExtra("releasedate");
        String mDetail = intent.getStringExtra("description");
        final double rating = intent.getDoubleExtra("rating", 0);
        int theId = intent.getIntExtra("movieId", 0);

        TextView ratingText = (TextView) findViewById(R.id.rating);
        ratingText.setText(String.valueOf(rating));
        ImageView imageView = (ImageView) findViewById(R.id.posterimage);
        Picasso.with(getBaseContext()).load(imagePoster).into(imageView);
        TextView textView = (TextView) findViewById(R.id.titleText);
        textView.setText(movieName);
        textView.setVisibility(View.VISIBLE);
        TextView r = (TextView) findViewById(R.id.reDate);
        r.setVisibility(View.VISIBLE);
        r.setText(rDate);
        TextView textView2 = (TextView) findViewById(R.id.movieDesciption);
        textView2.setText(mDetail);

        BuiltUrl(String.valueOf(theId), "videos");
        BuiltUrl(String.valueOf(theId), "reviews");
        star=(ImageView)findViewById(R.id.starAdd);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            int checkResult=   checkData(movieName);
                if(checkResult==1) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.MovieEntry.Movie_Name, movieName);
                    contentValues.put(MovieContract.MovieEntry.Movie_Poster_Path, imagePoster);
                    contentValues.put(MovieContract.MovieEntry.Movie_Rating, rating);
                    contentValues.put(MovieContract.MovieEntry.Release_Date, rDate);


                    Uri uriInserted = getContentResolver().insert(MovieContract.MovieEntry.ContentUri, contentValues);
                    if (uriInserted != null) {
                        Toast.makeText(DetailedList.this, "Added To favourties", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(DetailedList.this,"Movie Already Exist",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    private int checkData(String mname)
    {
        int f=0;
        MovieDbHelper movieDbHelper=new MovieDbHelper(DetailedList.this);
        String query="SELECT * FROM "+ MovieContract.MovieEntry.Table_Name;
        SQLiteDatabase sqLiteDatabase=movieDbHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            while(cursor.moveToNext())
            {
                if(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.Movie_Name)).equalsIgnoreCase(mname))
                {
                f++;
                    break;
                }
            }
        }
        cursor.moveToFirst();
        if(f==0)
        {
            return 1;
        }
        else
        {
            f=0;
            return 0;
        }

    }

    private void BuiltUrl(String id, String pref) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(id)
                .appendPath(pref)
                .appendQueryParameter("api_key", "7b9c9e48d707b60dedb57aebfb1ec4e3");
        String builturl = builder.build().toString();
        URL url = null;
        try {
            url = new URL(builturl);

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "BuiltUrl: ");
        }
        if (pref.equalsIgnoreCase("videos")) {
            new TrailerAsynTask().execute(url);
        } else {
            new ReviewAsyn().execute(url);
        }
    }

    private String makeJasonString(URL url) {
        String jsonResponse = "";
        if (url == null) {
            return null;
        }
        try {
            HttpURLConnection urlConnection;
            InputStream inputStream;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(150000);
            urlConnection.setReadTimeout(100000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                StringBuilder output = new StringBuilder();
                if (inputStream != null) {
                    InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                    BufferedReader bufferReader = new BufferedReader(streamReader);
                    String line = bufferReader.readLine();
                    while (line != null) {
                        output.append(line);
                        line = bufferReader.readLine();
                    }
                }
                jsonResponse = output.toString();

            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "makeJasonString: ");
        }

        return jsonResponse;
    }

    private void plavideo(String key) {
        Intent forapp = new Intent(Intent.ACTION_VIEW);
        forapp.setData(Uri.parse(key));
        if (forapp.resolveActivity(getPackageManager()) != null) {
            startActivity(forapp);
        }

    }

    private class ReviewAsyn extends AsyncTask<URL, Void, ArrayList<Reviewdata>> {

        @Override
        protected ArrayList<Reviewdata> doInBackground(URL... urls) {
            ArrayList<Reviewdata> reviews = new ArrayList<Reviewdata>();
            URL url = urls[0];
            String jsonResponse = makeJasonString(url);
            try {

                JSONObject baseStringObject = new JSONObject(jsonResponse);

                JSONArray rejultArray = baseStringObject.getJSONArray("results");

                for (int i = 0; i <= rejultArray.length(); i++) {

                    JSONObject jo = rejultArray.getJSONObject(i);


                    String author = jo.getString("author");
                    String review = jo.getString("content");
                    String reviewurl = jo.getString("url");
                    Reviewdata reviewdata = new Reviewdata(author, review, reviewurl);
                    reviews.add(reviewdata);


                }


            } catch (JSONException e) {
                Log.e(LOG_TAG, "doInBackground: ");
            }
            return reviews;
        }


        @Override
        protected void onPostExecute(ArrayList<Reviewdata> reviewdatas) {

            ReviewAdapter reviewAdapter = new ReviewAdapter(DetailedList.this, reviewdatas);
            NonScrollListView nonScrollListView = (NonScrollListView) findViewById(R.id.reviewlist);

            nonScrollListView.setAdapter(reviewAdapter);
            nonScrollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Reviewdata reviewdata = (Reviewdata) adapterView.getItemAtPosition(i);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(reviewdata.getUrl()));
                    startActivity(intent);

                }
            });
            progressBar.setVisibility(View.GONE);


        }
    }

    private class TrailerAsynTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected String[] doInBackground(URL... urls) {

            URL url = urls[0];
            String[] key = null;

            String jsonstring = makeJasonString(url);
            try {
                JSONObject baseStringObject = new JSONObject(jsonstring);

                JSONArray rejultArray = baseStringObject.getJSONArray("results");

                for (int i = 0; i < rejultArray.length(); i++) {
                    JSONObject jo = rejultArray.getJSONObject(i);
                    if (jo.has("key"))

                    {
                        key = new String[rejultArray.length()];
                        key[i] = jo.getString("key");
                    }

                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, "doInBackground: ");
            }


            return key;
        }

        @Override
        protected void onPostExecute(final String[] strings) {
            if (strings == null) {
                noTrailer.setVisibility(View.VISIBLE);
            } else {

                for (int i = 0; i < strings.length; i++) {
                    if (!TextUtils.isEmpty(strings[i])) {
                        String thubnail = "https://img.youtube.com/vi/" + strings[i] + "/0.jpg";
                        final String movieUrl = "https://www.youtube.com/watch?v=" + strings[i];
                        Picasso.with(getBaseContext()).load(thubnail).into(imageThumb);
                        icon.setVisibility(View.VISIBLE);
                        imageThumb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                plavideo(movieUrl);
                            }
                        });
                        icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                plavideo(movieUrl);
                            }
                        });

                    }

                }
            }
        }

    }

}
