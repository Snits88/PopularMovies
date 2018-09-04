package com.android.angelo.popularmovies;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.angelo.popularmovies.Model.MovieListTO;
import com.android.angelo.popularmovies.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

//    final private ListItemClickListener mOnClickListener;
    private int nMovieItems;
    private MovieListTO movieListTO;


    public MoviesAdapter(int numberOfItems, MovieListTO movieListTO){
        this.nMovieItems = numberOfItems;
//        this.mOnClickListener = clickListener;
        setMovieListTO(movieListTO);
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MovieViewHolder mvh = new MovieViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if(movieListTO.getMovies() != null){
            Uri uri = NetworkUtils.buildImageUrl(movieListTO.getMovies().get(position).getPoster_path());
            Picasso.with(holder.mImagePosterView.getContext())
                    .load(uri)
                    .into(holder.mImagePosterView);
        }
    }

    @Override
    public int getItemCount() {
        return nMovieItems;
    }

    public MovieListTO getMovieListTO() {
        return movieListTO;
    }

    public void setMovieListTO(MovieListTO movieListTO) {
        this.movieListTO = movieListTO;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView mImagePosterView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mImagePosterView = itemView.findViewById(R.id.imageView);

//            itemView.setOnClickListener(this);
        }

        /**
         * Called whenever a user clicks on an item in the list.
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
//            mOnClickListener.onListItemClick(clickedPosition);
        }
    }


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
