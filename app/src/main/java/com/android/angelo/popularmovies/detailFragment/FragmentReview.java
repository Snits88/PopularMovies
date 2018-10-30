package com.android.angelo.popularmovies.detailFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.angelo.popularmovies.adapter.MoviesReviewAdapter;
import com.android.angelo.popularmovies.model.MovieReviewListTO;
import com.android.angelo.popularmovies.model.MovieTO;
import com.android.angelo.popularmovies.R;
import com.android.angelo.popularmovies.utils.RetrofitClientInstance;
import com.android.angelo.popularmovies.utils.movieDBInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.android.angelo.popularmovies.adapter.MyFragmentAdapter.MOVIE_KEY;

public class FragmentReview extends Fragment {

    private String api_key;
    private MovieTO movie;
    private int page_number = 1;
    private int total_page = 1;
    private movieDBInterface mDBInterface;
    private MovieReviewListTO mReviewListTO;
    private boolean isLoading = true;
    private RecyclerView recyclerView;
    private MoviesReviewAdapter mReviewAdapter;

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

    private void callAPIMovieReviews(movieDBInterface mDBInterface) {
        isLoading = true;
        Call<MovieReviewListTO> allMovieReviews = mDBInterface.getAllMovieReviews(movie.getId(), api_key, page_number);
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
        menageRecyclerView(rootView);
        callAPIMovieReviews(mDBInterface);
        return rootView;
    }

    private void menageRecyclerView(View rootView) {
        //Manage RecyclerView
        recyclerView = rootView.findViewById(R.id.review_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager lm = (LinearLayoutManager)recyclerView.getLayoutManager();

                int visibleItemCount = lm.getChildCount();
                int totalItemCount = lm.getItemCount();
                int firstVisibleItemPosition = lm.findFirstVisibleItemPosition();

                if (isLoading == false && page_number < total_page) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        callAPIMovieReviews(mDBInterface);
                    }
                }
            }
        });
        // Create and menage Adapter for the Recycler view
        mReviewAdapter = new MoviesReviewAdapter();
        recyclerView.setAdapter(mReviewAdapter);
    }

    public void generateDataList(MovieReviewListTO reviews){
        mReviewListTO = reviews;
        page_number++;
        total_page = reviews.getTotalPages();
        isLoading = false;
        mReviewAdapter.getMovieReviewList().addAll(reviews.getResults());
        mReviewAdapter.notifyDataSetChanged();
    }
}
