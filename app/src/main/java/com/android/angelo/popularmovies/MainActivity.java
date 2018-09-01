package com.android.angelo.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.angelo.popularmovies.Model.MovieListTO;
import com.android.angelo.popularmovies.Utils.JsonUtils;
import com.android.angelo.popularmovies.Utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

public class MainActivity extends AppCompatActivity
        implements MoviesAdapter.ListItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int NUM_LIST_ITEMS = 20;

    private MoviesAdapter mAdapter;
    private RecyclerView mMoviesList;
    private ProgressBar mProgressBar;
    private ImageView mImageError;
    private String key;
    private String language;
    private static final int FIRST_PAGE_NUMBER = 1;
    private static final String FIRST_REST_CALL = "movie/popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageError = findViewById(R.id.image_error);
        mImageError.setImageResource(R.drawable.server_error);
        mProgressBar = findViewById(R.id.main_progress);

        mMoviesList = findViewById(R.id.main_recycler);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMoviesList.setLayoutManager(layoutManager);

        mMoviesList.setHasFixedSize(true);

        mAdapter = new MoviesAdapter(NUM_LIST_ITEMS, this);
        mMoviesList.setAdapter(mAdapter);

        key = getString(R.string.api_key);
        language = getString(R.string.language);
        firstLoadMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }

    public void firstLoadMovies(){
        Log.d(TAG, "First Load Movies...");
        new DownloadAsyncTask().execute(FIRST_REST_CALL,key,language);
    }

    public class DownloadAsyncTask extends AsyncTask<String, Void, MovieListTO> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mImageError.setVisibility(View.GONE);
        }

        @Override
        protected MovieListTO doInBackground(String... infoRestCall) {
            URL url = NetworkUtils.buildUrl(infoRestCall[0],infoRestCall[1],infoRestCall[2],FIRST_PAGE_NUMBER);
            try {
                JSONObject jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                return JsonUtils.parseJsonToMovieListTO(jsonResponse);
            } catch (IOException | JSONException | ParseException e) {
                mImageError.setVisibility(View.VISIBLE);
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieListTO result) {
            mProgressBar.setVisibility(View.GONE);
            mAdapter.setMovieListTO(result);
        }
    }

}
