package com.android.angelo.popularmovies.converter;

import com.android.angelo.popularmovies.database.MovieEntry;
import com.android.angelo.popularmovies.model.MovieTO;

public class MovieTOToMovieEntityConverter {

    public static MovieEntry convert(MovieTO movieTO){
        MovieEntry movieEntry = new MovieEntry();
        if(movieTO != null){
            movieEntry.setCode(movieTO.getId());
            movieEntry.setAdult(movieTO.isAdult());
            movieEntry.setBackdropPath(movieTO.getBackdrop_path());
            movieEntry.setOriginalLanguage(movieTO.getOriginal_language());
            movieEntry.setTitle(movieTO.getTitle());
            movieEntry.setOriginalTitle(movieTO.getOriginal_title());
            movieEntry.setPopularity(movieTO.getPopularity());
            movieEntry.setPosterPath(movieTO.getPoster_path());
            movieEntry.setVoteAvarage(movieTO.getVote_avarage());
            movieEntry.setVoteCount(movieTO.getVote_count());
            movieEntry.setOverview(movieTO.getOverview());
            movieEntry.setReleaseDate(movieTO.getRelease_date());
        }
        return movieEntry;
    }
}