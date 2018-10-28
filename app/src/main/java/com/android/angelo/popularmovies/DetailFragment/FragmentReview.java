package com.android.angelo.popularmovies.DetailFragment;

import android.arch.core.BuildConfig;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.angelo.popularmovies.Model.MovieReviewListTO;
import com.android.angelo.popularmovies.Model.MovieTO;
import com.android.angelo.popularmovies.R;
import com.android.angelo.popularmovies.Utils.RetrofitClientInstance;
import com.android.angelo.popularmovies.Utils.movieDBInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.android.angelo.popularmovies.Adapter.MyFragmentAdapter.MOVIE_KEY;

public class FragmentReview extends Fragment {

    private String api_key;
    private MovieTO movie;
    private MovieReviewListTO mReviewListTO;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        movie = args.getParcelable(MOVIE_KEY);
        api_key = com.android.angelo.popularmovies.BuildConfig.API_KEY;
        /*Create handle for the RetrofitInstance interface*/
        Retrofit retrofitInstance = RetrofitClientInstance.getRetrofitInstance();
        movieDBInterface mDBInterface = retrofitInstance.create(movieDBInterface.class);
        Call<MovieReviewListTO> allMovieReviews = mDBInterface.getAllMovieReviews(movie.getId(), api_key);
        allMovieReviews.enqueue(new Callback<MovieReviewListTO>() {
            @Override
            public void onResponse(Call<MovieReviewListTO> call, Response<MovieReviewListTO> response) {
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<MovieReviewListTO> call, Throwable t) {
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
                R.layout.fragment_review, container, false);
        return rootView;
    }

    public void generateDataList(MovieReviewListTO reviews){
        mReviewListTO = reviews;
    }
}
