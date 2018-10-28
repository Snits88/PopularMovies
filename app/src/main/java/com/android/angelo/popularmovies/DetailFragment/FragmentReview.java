package com.android.angelo.popularmovies.DetailFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.angelo.popularmovies.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentReview extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //Call Here AsyncTaskLoader to load review
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_review, container, false);
        Bundle args = getArguments();
        return rootView;
    }
}
