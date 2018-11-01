package com.android.angelo.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.android.angelo.popularmovies.database.AppDatabase;
import com.android.angelo.popularmovies.database.MovieEntry;

public class AddMovieViewModel extends ViewModel {
    private LiveData<MovieEntry> movieEntry;

    public AddMovieViewModel(AppDatabase database, int movieId) {
        movieEntry = database.taskDao().loadMovieEntryById(movieId);
    }

    public LiveData<MovieEntry> getMovie() {
        return movieEntry;
    }
}
