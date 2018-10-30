package com.android.angelo.popularmovies.Utils;

import com.android.angelo.popularmovies.Model.MovieReviewListTO;
import com.android.angelo.popularmovies.Model.MovieTrailerListTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface movieDBInterface {

    @GET("movie/{id}/reviews")
    Call<MovieReviewListTO> getAllMovieReviews(@Path("id") int id, @Query("api_key") String api_key, @Query("page_number") int page_number);

    @GET("movie/{id}/videos")
    Call<MovieTrailerListTO> getAllMovieTrailers(@Path("id") int id, @Query("api_key") String api_key);

}
