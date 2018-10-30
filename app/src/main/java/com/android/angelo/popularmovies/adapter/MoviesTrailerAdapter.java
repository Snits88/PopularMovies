package com.android.angelo.popularmovies.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.android.angelo.popularmovies.model.MovieTrailerTO;
import com.android.angelo.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesTrailerAdapter  extends RecyclerView.Adapter<MoviesTrailerAdapter.MovieTrailerViewHolder>{

    final private ClickListener mOnClickListener;
    private List<MovieTrailerTO> movieTrailerList;
    private final static String SITE = "YouTube";
    private final static String BASE_URL_TRAILER_IMAGE = "https://img.youtube.com/vi/";
    private final static String BASE_URL_TRAILER = "https://www.youtube.com/watch";
    private final static String QUERY_PARAM = "v=";

    public MoviesTrailerAdapter(ClickListener clickListener){
        setMovieTrailerList(new ArrayList<MovieTrailerTO>());
        mOnClickListener = clickListener;
    }

    @NonNull
    @Override
    public MoviesTrailerAdapter.MovieTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.fragment_trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MoviesTrailerAdapter.MovieTrailerViewHolder mtvh = new MoviesTrailerAdapter.MovieTrailerViewHolder(view);
        return mtvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesTrailerAdapter.MovieTrailerViewHolder holder, int position) {
        if(movieTrailerList.size() > 0){
            if(SITE.equalsIgnoreCase(movieTrailerList.get(position).getSite())){
                //Uri for load Image fpr Trailer
                Uri uriImage = Uri.parse(BASE_URL_TRAILER_IMAGE).buildUpon().appendPath(movieTrailerList.get(position).getKey()).appendEncodedPath("0.jpg").build();
                Picasso.with(holder.trailerImageView.getContext())
                        .load(uriImage)
                        .into(holder.trailerImageView);
            }else{
                holder.trailerImageView.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return getMovieTrailerList().size();
    }

    public class MovieTrailerViewHolder extends RecyclerView.ViewHolder {
        ImageView trailerImageView;
        WebView trailerWebView;
        public MovieTrailerViewHolder(View itemView) {
            super(itemView);
            trailerImageView = itemView.findViewById(R.id.imageViewTrailer);
            trailerImageView.setClickable(true);
            trailerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Uri For view Trailer
                    String urlTrailer = BASE_URL_TRAILER + "?" + QUERY_PARAM + movieTrailerList.get(getAdapterPosition()).getKey();
                    mOnClickListener.onListItemClick(urlTrailer);
                }
            });



        }
    }

    public List<MovieTrailerTO> getMovieTrailerList() {
        return movieTrailerList;
    }

    public void setMovieTrailerList(List<MovieTrailerTO> movieTrailerList) {
        this.movieTrailerList = movieTrailerList;
    }

    public interface ClickListener {
        void onListItemClick(String urlTrailer);
    }

}
