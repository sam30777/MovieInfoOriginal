package com.example.android.movieinfo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    GridView gridView;
    movieAdapter movieAdapter;


    MovieCursorAdapter movieCursorAdapter;
    int loaderid = 1;
    private String movieUrl;
    private String current_Scroll="scroll_pos";
    private  String my_Prefrence="prefrence";
    String sorting;
    int current_Position;
    @Override
    protected void onSaveInstanceState(Bundle outState) {


        outState.putString(my_Prefrence,sorting);
        outState.putInt(current_Scroll,gridView.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gridView = (GridView) findViewById(R.id.moviesGrid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailedList.class);
                movieData mydata = (movieData) adapterView.getItemAtPosition(i);
                intent.putExtra(getString(R.string.poster), mydata.getImage().toString());
                intent.putExtra(getString(R.string.title), mydata.getTitle().toString());
                intent.putExtra(getString(R.string.rdate), mydata.getDate().toString());
                intent.putExtra(getString(R.string.desc), mydata.getMovieDesc().toString());
                intent.putExtra(getString(R.string.movieid), mydata.getMid());
                intent.putExtra(getString(R.string.tr), mydata.getTrailerString());
                intent.putExtra(getString(R.string.rting), mydata.getRating());

                startActivity(intent);
            }
        });
        if(savedInstanceState!=null)
        {
            current_Position=  savedInstanceState.getInt(current_Scroll,0);
            BuilduriAccordingly(savedInstanceState.getString(my_Prefrence));

        }
        else
        { BuilduriAccordingly("now_playing");}
    }


      private void BuilduriAccordingly(String value) {

        movieUrl = "https://api.themoviedb.org/3/movie/"+value+"?api_key=7b9c9e48d707b60dedb57aebfb1ec4e3&language=en-US&page=1";
          ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
          NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
          if(networkInfo!=null&&networkInfo.isConnected())
          {

              new MovieAsyn().execute(movieUrl);

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
            Log.e(LOG_TAG, getString(R.string.makejason));
        }

        return jsonResponse;
    }

    private URL makeUrl(String mUrlString) {
        URL myurl = null;
        try {
            myurl = new URL(mUrlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, getString(R.string.makeurl));
        }
        return myurl;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sortoptions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sortByPopular) {
       BuilduriAccordingly("popular");
            sorting="popular";
            Toast.makeText(this,"Prefrence Changed",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.sortByRating) {
           BuilduriAccordingly("top_rated");
            sorting="top_rated";
            Toast.makeText(this,"Prefrence Changed",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.sortByFavourate) {
            Intent intent = new Intent(MainActivity.this, Favourate.class);
            startActivity(intent);
        }

        return true;
    }
    private class MovieAsyn extends AsyncTask<String, Void, ArrayList<movieData>> {

        @Override
        protected ArrayList<movieData> doInBackground(String... urls) {


            ArrayList<movieData> finallist = new ArrayList<movieData>();


            URL url = makeUrl(urls[0]);

            String jsonResponse = makeJasonString(url);


            if (TextUtils.isEmpty(jsonResponse)) {
                return null;
            }
            try {

                JSONObject base = new JSONObject(jsonResponse);
                JSONArray baseArray = base.getJSONArray("results");

                for (int i = 0; i < baseArray.length(); i++) {
                    JSONObject baseArrayObject = baseArray.getJSONObject(i);
                    if (baseArrayObject.has("poster_path")) {
                        String image = "https://image.tmdb.org/t/p/w185//" + baseArrayObject.getString("poster_path");
                        String mName = baseArrayObject.getString("title");
                        String description = baseArrayObject.getString("overview");
                        String releaseDate = baseArrayObject.getString("release_date");
                        String rating = baseArrayObject.getString("vote_average");
                        Double ratingMovie = Double.parseDouble(rating);
                        int movieId = baseArrayObject.getInt("id");

                        movieData data = new movieData(mName, description, releaseDate, ratingMovie, image, movieId);
                        finallist.add(data);
                    }


                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "doInBackground: ");
            }


            return finallist;
        }

        @Override
        protected void onPostExecute(ArrayList<movieData> movieDatas) {
            if(movieDatas==null)
            {
                TextView textView=(TextView)findViewById(R.id.emptylist);
                gridView.setEmptyView(textView);
            }
            movieAdapter = new movieAdapter(MainActivity.this, movieDatas);

                        gridView.setAdapter(movieAdapter);


                    gridView.smoothScrollToPosition(current_Position);


        }
    }


}

