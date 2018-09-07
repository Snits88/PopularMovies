package com.android.angelo.popularmovies;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.angelo.popularmovies.Model.MovieListTO;
import com.android.angelo.popularmovies.Model.MovieTO;
import com.android.angelo.popularmovies.Utils.JsonUtils;
import com.android.angelo.popularmovies.Utils.NetworkUtils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

public class MainActivity extends AppCompatActivity
        implements MoviesAdapter.ListItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int NUM_LIST_ITEMS = 20;
    private static final String TOP_RATED = "top_rated";
    private static final String MOST_POPULAR = "most_popular";

    private MoviesAdapter mAdapter;
    private RecyclerView mMoviesList;
    private ProgressBar mProgressBar;
    private ImageView mImageError;
    private String key;
    private String language;
    private int page_number = 1;
    private int total_page = 1;
    private boolean isLoading = false;
    private boolean reset_call = false;
    private String typology_call = MOST_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageError = findViewById(R.id.image_error);
        mImageError.setImageResource(R.drawable.server_error);
        mProgressBar = findViewById(R.id.main_progress);
        mMoviesList = findViewById(R.id.main_recycler);

        key = getString(R.string.api_key);
        language = getString(R.string.language);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMoviesList.setLayoutManager(layoutManager);

        mMoviesList.setHasFixedSize(true);
        mAdapter = new MoviesAdapter(NUM_LIST_ITEMS, new MovieListTO(), this);
        mMoviesList.setAdapter(mAdapter);
        firstLoadMovies();
        mMoviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager lm = (GridLayoutManager)recyclerView.getLayoutManager();

                int visibleItemCount = lm.getChildCount();
                int totalItemCount = lm.getItemCount();
                int firstVisibleItemPosition = lm.findFirstVisibleItemPosition();

                if (isLoading == false && page_number < total_page) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        new DownloadAsyncTask().execute(key,language);
                    }
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_most_popular) {
            Toast.makeText(this, "Most Popular Movies!", Toast.LENGTH_LONG).show();
            typology_call = MOST_POPULAR;
        }
        if(id == R.id.action_top_rated){
            Toast.makeText(this, "Top Rated Movies!", Toast.LENGTH_LONG).show();
            typology_call = TOP_RATED;
        }
        reset_call = true;
        new DownloadAsyncTask().execute(key,language);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(MovieTO clickedMovie) {
        Toast.makeText(this, clickedMovie.getTitle(), Toast.LENGTH_LONG).show();
        //TODO: add Intent and call a new Activity with Film Info
    }



    public void firstLoadMovies(){
        Log.d(TAG, "First Load Movies...");
        new DownloadAsyncTask().execute(key,language);
    }

    public class DownloadAsyncTask extends AsyncTask<Object, Void, MovieListTO> {

        @Override
        protected void onPreExecute() {
            isLoading = true;
            mProgressBar.setVisibility(View.VISIBLE);
            mImageError.setVisibility(View.GONE);
            if(reset_call){
                page_number = 1;
                mAdapter.setMovieListTO(new MovieListTO());
                mAdapter.notifyDataSetChanged();
                reset_call = false;
            }
        }

        @Override
        protected MovieListTO doInBackground(Object... infoRestCall) {
            URL url = null;
            if(StringUtils.equals(typology_call, MOST_POPULAR)){
                url = NetworkUtils.buildPopularMoviesUrl((String)infoRestCall[0],(String)infoRestCall[1],page_number);
            }
            if(StringUtils.equals(typology_call, TOP_RATED)){
                url = NetworkUtils.buildTopRatedUrl((String)infoRestCall[0],(String)infoRestCall[1], page_number);
            }
            try {
                JSONObject jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                return JsonUtils.parseJsonToMovieListTO(jsonResponse);
            } catch (IOException | JSONException | ParseException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieListTO result) {
            mProgressBar.setVisibility(View.GONE);
            if(result == null){
                mImageError.setVisibility(View.VISIBLE);
            }else{
                if(result.getMovies() != null && result.getMovies().size() > 0){
                    if(mAdapter.getMovieListTO().getMovies() != null){
                        mAdapter.getMovieListTO().getMovies().addAll(result.getMovies());
                    }else{
                        mAdapter.setMovieListTO(result);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                total_page = result.getTotal_pages();
            }
            page_number++;
            isLoading = false;
        }
    }
}
