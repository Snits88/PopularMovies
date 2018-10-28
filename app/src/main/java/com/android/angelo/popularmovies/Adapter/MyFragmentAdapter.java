package com.android.angelo.popularmovies.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.angelo.popularmovies.DetailFragment.FragmentOverview;
import com.android.angelo.popularmovies.DetailFragment.FragmentReview;
import com.android.angelo.popularmovies.DetailFragment.FragmentTrailer;
import com.android.angelo.popularmovies.Model.MovieTO;


public class MyFragmentAdapter extends FragmentStatePagerAdapter {

    private MovieTO movieTO;
    public static final String MOVIE_KEY = "MOVIE_KEY";

    public MyFragmentAdapter(FragmentManager fm, MovieTO movieTO){
        super(fm);
        this.movieTO = movieTO;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_KEY, movieTO);
        switch (position){
            case 0 :
                fragment = new FragmentOverview();
                break;
            case 1 :
                fragment = new FragmentReview();
                break;
            case 2:
                fragment = new FragmentTrailer();
                break;
        }
        if(fragment != null) fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        switch (position){
            case 0 :
                title =  "Overview";
                break;
            case 1 :
                title = "Review";
                break;
            case 2 :
                title =  "Trailer";
                break;
        }
        return title;
    }
}
