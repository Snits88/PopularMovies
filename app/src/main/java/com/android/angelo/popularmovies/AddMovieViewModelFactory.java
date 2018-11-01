package com.android.angelo.popularmovies;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.android.angelo.popularmovies.database.AppDatabase;

public class AddMovieViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final AppDatabase mDatabase;
    private final int moviedId;

    public AddMovieViewModelFactory(AppDatabase database, int mId) {
        mDatabase = database;
        moviedId = mId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddMovieViewModel(mDatabase, moviedId);
    }
}
