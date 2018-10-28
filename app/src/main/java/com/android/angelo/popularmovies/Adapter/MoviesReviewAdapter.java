package com.android.angelo.popularmovies.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.android.angelo.popularmovies.Model.MovieReviewListTO;

public class MoviesReviewAdapter extends RecyclerView.Adapter<MoviesReviewAdapter.MovieReviewViewHolder>{

    private int nMovieReviewItems;
    private MovieReviewListTO movieReviewListTO;

    @NonNull
    @Override
    public MovieReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return movieReviewListTO.getResults() == null ? nMovieReviewItems : movieReviewListTO.getResults().size();
    }

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder {

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
        }
    }

}
