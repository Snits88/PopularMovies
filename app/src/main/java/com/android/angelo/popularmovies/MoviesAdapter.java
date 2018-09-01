package com.android.angelo.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    final private ListItemClickListener mOnClickListener;
    private static int viewHolderCount;
    private int nMovieItems;


    public MoviesAdapter(int numberOfItems, ListItemClickListener listener){
        nMovieItems = numberOfItems;
        mOnClickListener = listener;
        viewHolderCount = 0;
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return nMovieItems;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        TextView listItemMovieView;
        // Will display which ViewHolder is displaying this data
        TextView viewHolderIndex;

        public MovieViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        /**
         * Called whenever a user clicks on an item in the list.
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
