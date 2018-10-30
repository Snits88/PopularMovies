package com.android.angelo.popularmovies.DetailFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.angelo.popularmovies.Adapter.MoviesTrailerAdapter;
import com.android.angelo.popularmovies.Model.MovieTO;
import com.android.angelo.popularmovies.Model.MovieTrailerListTO;
import com.android.angelo.popularmovies.R;
import com.android.angelo.popularmovies.Utils.RetrofitClientInstance;
import com.android.angelo.popularmovies.Utils.movieDBInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.android.angelo.popularmovies.Adapter.MyFragmentAdapter.MOVIE_KEY;

public class FragmentTrailer extends Fragment {

    private String api_key;
    private MovieTO movie;
    private movieDBInterface mDBInterface;
    private MovieTrailerListTO mTrailerListTO;
    private RecyclerView recyclerView;
    private MoviesTrailerAdapter mTrailerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        movie = args.getParcelable(MOVIE_KEY);
        api_key = com.android.angelo.popularmovies.BuildConfig.API_KEY;
        /*Create handle for the RetrofitInstance interface*/
        Retrofit retrofitInstance = RetrofitClientInstance.getRetrofitInstance();
        mDBInterface = retrofitInstance.create(movieDBInterface.class);
    }

    private void callAPIMovieTrailers(movieDBInterface mDBInterface) {
        Call<MovieTrailerListTO> allMovieTrailers = mDBInterface.getAllMovieTrailers(movie.getId(), api_key);
        allMovieTrailers.enqueue(new Callback<MovieTrailerListTO>() {
            @Override
            public void onResponse(Call<MovieTrailerListTO> call, Response<MovieTrailerListTO> response) {
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<MovieTrailerListTO> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_trailer, container, false);
        menageRecyclerView(rootView);
        callAPIMovieTrailers(mDBInterface);
        return rootView;
    }

    private void menageRecyclerView(View rootView) {
        //Manage RecyclerView
        recyclerView = rootView.findViewById(R.id.trailer_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        // Create and menage Adapter for the Recycler view
        mTrailerAdapter = new MoviesTrailerAdapter();
        recyclerView.setAdapter(mTrailerAdapter);
    }

    public void generateDataList(MovieTrailerListTO trailers){
        mTrailerListTO = trailers;
        mTrailerAdapter.getMovieTrailerList().addAll(trailers.getResults());
        mTrailerAdapter.notifyDataSetChanged();
    }

}
