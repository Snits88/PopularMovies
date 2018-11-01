package com.android.angelo.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.android.angelo.popularmovies.adapter.MoviesAdapter;
import com.android.angelo.popularmovies.converter.MovieEntityToMovieTOConverter;
import com.android.angelo.popularmovies.database.AppDatabase;
import com.android.angelo.popularmovies.database.MovieEntry;
import com.android.angelo.popularmovies.model.MovieListTO;
import com.android.angelo.popularmovies.model.MovieTO;
import com.android.angelo.popularmovies.utils.JsonUtils;
import com.android.angelo.popularmovies.utils.NetworkUtils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MoviesAdapter.ListItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int NUM_LIST_ITEMS = 20;
    private static final String TOP_RATED = "top_rated";
    private static final String MOST_POPULAR = "most_popular";
    private static final String FAVOURITES = "favourites";

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

    private AppDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageError = findViewById(R.id.image_error);
        mImageError.setImageResource(R.drawable.server_error);
        mProgressBar = findViewById(R.id.main_progress);
        mMoviesList = findViewById(R.id.main_recycler);

        key = BuildConfig.API_KEY;
        language = getString(R.string.language);
        
        //Menage DB Configuration
        mDatabase = AppDatabase.getInstance(getApplicationContext());
        
        //Manage Recycler View Adapter
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMoviesList.setLayoutManager(layoutManager);
        mMoviesList.setHasFixedSize(true);
        mAdapter = new MoviesAdapter(NUM_LIST_ITEMS, new MovieListTO(), this);
        mMoviesList.setAdapter(mAdapter);
        if(!StringUtils.equalsIgnoreCase(typology_call, FAVOURITES)){
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
        if(id == R.id.action_favourites){
            Toast.makeText(this, "Favourites Movies!", Toast.LENGTH_LONG).show();
            typology_call = FAVOURITES;
            resetItemMovieAdapter();
            setupViewModel();
            return super.onOptionsItemSelected(item);
        }
        reset_call = true;
        new DownloadAsyncTask().execute(key,language);
        return super.onOptionsItemSelected(item);
    }

    private void resetItemMovieAdapter() {
        MovieListTO newl = new MovieListTO();
        newl.setMovies(new ArrayList<MovieTO>());
        mAdapter.setMovieListTO(newl);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(int position, MovieTO clickedMovie) {
        launchDetailActivity(position, clickedMovie);
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

    private void launchDetailActivity(int position, MovieTO clickedMovie) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DetailActivity.MOVIE_INFO, clickedMovie);
        bundle.putInt(DetailActivity.EXTRA_POSITION, position);
        intent.putExtra(DetailActivity.INFO, bundle);
        startActivity(intent);
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getEntries().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> mEntries) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                //Convert Entity Object to Business Object
                MovieListTO mListTO = getMovieListTOFromEntryOnDB(mEntries);
                mAdapter.setMovieListTO(mListTO);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @NonNull
    private MovieListTO getMovieListTOFromEntryOnDB(@Nullable List<MovieEntry> mEntries) {
        MovieListTO mListTO = new MovieListTO();
        mListTO.setMovies(new ArrayList<MovieTO>());
        for (MovieEntry mEntry : mEntries) {
            MovieTO mTO = MovieEntityToMovieTOConverter.convert(mEntry);
            mListTO.getMovies().add(mTO);
        }
        return mListTO;
    }

    @Override
    public void onStop(){
        super.onStop();
        reset_call = true;
    }

    @Override
    public void onResume(){
        super.onResume();
        firstLoadMovies();
    }
}
