package com.android.angelo.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.angelo.popularmovies.model.MovieReviewTO;
import com.android.angelo.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

public class MoviesReviewAdapter extends RecyclerView.Adapter<MoviesReviewAdapter.MovieReviewViewHolder>{

    private List<MovieReviewTO> movieReviewList;

    public MoviesReviewAdapter(){
        setMovieReviewList(new ArrayList<MovieReviewTO>());
    }

    @NonNull
    @Override
    public MovieReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.fragment_review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MoviesReviewAdapter.MovieReviewViewHolder mrvh = new MoviesReviewAdapter.MovieReviewViewHolder(view);
        return mrvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewViewHolder holder, int position) {
        if(movieReviewList.size() > 0){
            holder.reviewTextView.setText(movieReviewList.get(position).getAuthor() + " says: \n\n" + movieReviewList.get(position).getContent());
        }
    }

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewTextView;
        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            reviewTextView = itemView.findViewById(R.id.reviewTextView);
        }
    }

    @Override
    public int getItemCount() {
        return getMovieReviewList().size();
    }

    public List<MovieReviewTO> getMovieReviewList() {
        return movieReviewList;
    }

    public void setMovieReviewList(List<MovieReviewTO> movieReviewList) {
        this.movieReviewList = movieReviewList;
    }

}
