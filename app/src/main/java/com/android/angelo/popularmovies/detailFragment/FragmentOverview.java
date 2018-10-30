package com.android.angelo.popularmovies.detailFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.angelo.popularmovies.adapter.MyFragmentAdapter;
import com.android.angelo.popularmovies.model.MovieTO;
import com.android.angelo.popularmovies.R;

import org.apache.commons.lang3.StringUtils;

public class FragmentOverview extends Fragment {

    private TextView overview;
    private MovieTO movie;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_overview, container, false);
        Bundle args = getArguments();
        movie = args.getParcelable(MyFragmentAdapter.MOVIE_KEY);
        overview = (TextView) rootView.findViewById(R.id.movie_overview);
        overview.setVisibility(View.GONE);
        if(!StringUtils.isEmpty(movie.getOverview())){
            overview.setVisibility(View.VISIBLE);
            overview.setText(movie.getOverview());
        }
        return rootView;
    }
}
