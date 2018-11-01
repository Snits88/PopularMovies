package com.android.angelo.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.angelo.popularmovies.adapter.MoviesTrailerAdapter;
import com.android.angelo.popularmovies.adapter.MyFragmentAdapter;
import com.android.angelo.popularmovies.converter.MovieEntityToMovieTOConverter;
import com.android.angelo.popularmovies.converter.MovieTOToMovieEntityConverter;
import com.android.angelo.popularmovies.database.AppDatabase;
import com.android.angelo.popularmovies.database.MovieEntry;
import com.android.angelo.popularmovies.model.MovieTO;
import com.android.angelo.popularmovies.utils.AppExecutors;
import com.android.angelo.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

public class DetailActivity extends AppCompatActivity
        implements MoviesTrailerAdapter.ClickListener{

    public static final String EXTRA_POSITION = "extra_position";
    public static final String MOVIE_INFO = "movie_info";
    public static final String INFO = "info";
    private static final int DEFAULT_POSITION = -1;

    private ViewPager mViewPager;
    private MyFragmentAdapter mFragmentAdapter;

    private AppDatabase mDatabase;
    private Button mFavourite;
    private static final String SAVE = "Save as Favourite";
    private static final String REMOVE = "Remove from Favourite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //Check Screen Rotation
        int orientation = this.getResources().getConfiguration().orientation;

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        ImageView poster = findViewById(R.id.poster);
        if (orientation == this.getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
            ingredientsIv.setVisibility(View.GONE);
        }
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(INFO);
        final MovieTO movie = bundle.getParcelable(MOVIE_INFO);

        //Menage movie for favourites
        mFavourite = findViewById(R.id.save_as_favourites);
        mDatabase = AppDatabase.getInstance(getApplicationContext());
        menageInsertDeleteFavourite(movie);

        if (movie == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }
        int position = bundle.getInt(EXTRA_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        // Fragment Management
        mFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(),movie,this);
        mViewPager =  (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setVisibility(View.VISIBLE);

        populateUI(movie);

        Uri uri = NetworkUtils.buildImageUrl(movie.getBackdrop_path());
        if(uri != null) {
            Picasso.with(this)
                    .load(uri)
                    .into(ingredientsIv);
        }
        Uri uriPoster = NetworkUtils.buildImageUrl(movie.getPoster_path());
        if(uriPoster != null) {
            Picasso.with(this)
                    .load(uriPoster)
                    .into(poster);
        }

        setTitle(movie.getTitle());
    }

    private void menageInsertDeleteFavourite(final MovieTO movie) {
        AddMovieViewModelFactory factory = new AddMovieViewModelFactory(mDatabase, movie.getId());
        final AddMovieViewModel viewModel = ViewModelProviders.of(this, factory).get(AddMovieViewModel.class);
        viewModel.getMovie().observe(this, new Observer<MovieEntry>() {
            @Override
            public void onChanged(@Nullable final MovieEntry mEntry) {
                mFavourite.setClickable(false);
                mFavourite.setVisibility(View.INVISIBLE);
                if (mEntry != null) {
                    mFavourite.setText(REMOVE);
                } else {
                    mFavourite.setText(SAVE);
                }
                mFavourite.setClickable(true);
                mFavourite.setVisibility(View.VISIBLE);
                addListenerFavourite(mEntry, movie);
            }
        });
    }

    private void addListenerFavourite(@Nullable final MovieEntry mEntry, final MovieTO movie) {
        mFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (SAVE.equals(mFavourite.getText())) {
                            MovieEntry entryInDB = MovieTOToMovieEntityConverter.convert(movie);
                            mDatabase.taskDao().insertTask(entryInDB);
                        } else if (REMOVE.equals(mFavourite.getText())) {
                            mDatabase.taskDao().deleteTask(mEntry);
                        }
                    }
                });
            }
        });
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(MovieTO movie) {
        TextView title = findViewById(R.id.movie_name);
        TextView date = findViewById(R.id.movie_release_date);
        TextView rating = findViewById(R.id.movie_rating);

        title.setText(movie.getTitle());
        rating.setText(String.valueOf(movie.getVote_avarage()) + "/10");
        Format formatter = new SimpleDateFormat("dd-MM-yyyy");
        String d = formatter.format(movie.getRelease_date());
        date.setText(d);
    }

    @Override
    public void onListItemClick(String urlTrailer){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlTrailer));
        // Verify it resolves
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;
        if(isIntentSafe) {startActivity(intent);}
    }
}
