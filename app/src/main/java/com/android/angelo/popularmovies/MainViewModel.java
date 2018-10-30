package com.android.angelo.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.android.angelo.popularmovies.database.AppDatabase;
import com.android.angelo.popularmovies.database.MovieEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<MovieEntry>> mEntries;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the movies from the DataBase");
        mEntries = database.taskDao().loadAllMovieEntry();
    }

    public LiveData<List<MovieEntry>> getEntries() {
        return mEntries;
    }
}
