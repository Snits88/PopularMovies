package com.android.angelo.popularmovies;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.angelo.popularmovies.Adapter.MyFragmentAdapter;
import com.android.angelo.popularmovies.Model.MovieTO;
import com.android.angelo.popularmovies.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    public static final String MOVIE_INFO = "movie_info";
    public static final String INFO = "info";
    private static final int DEFAULT_POSITION = -1;

    private ViewPager mViewPager;
    private MyFragmentAdapter mFragmentAdapter;

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
        MovieTO movie = bundle.getParcelable(MOVIE_INFO);
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
        mFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(),movie);
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
}
