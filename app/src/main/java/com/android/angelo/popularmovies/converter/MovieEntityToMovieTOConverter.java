package com.android.angelo.popularmovies.converter;

import com.android.angelo.popularmovies.database.MovieEntry;
import com.android.angelo.popularmovies.model.MovieTO;

public class MovieEntityToMovieTOConverter {

    public static MovieTO convert (MovieEntry movieEntry) {
        MovieTO movieTO = new MovieTO();
        if (movieEntry != null) {
            movieTO.setId(movieEntry.getCode());
            movieTO.setAdult(movieEntry.isAdult());
            movieTO.setBackdrop_path(movieEntry.getBackdropPath());
            movieTO.setOriginal_language(movieEntry.getOriginalLanguage());
            movieTO.setOriginal_title(movieEntry.getOriginalTitle());
            movieTO.setOverview(movieEntry.getOverview());
            movieTO.setPopularity(movieEntry.getPopularity());
            movieTO.setPoster_path(movieEntry.getPosterPath());
            movieTO.setRelease_date(movieEntry.getReleaseDate());
            movieTO.setTitle(movieEntry.getTitle());
            movieTO.setVote_avarage(movieEntry.getVoteAvarage());
            movieTO.setVote_count(movieEntry.getVoteCount());
        }
        return movieTO;
    }
}
