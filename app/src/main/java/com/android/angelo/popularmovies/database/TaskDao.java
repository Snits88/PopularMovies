package com.android.angelo.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM movie ORDER BY title")
    LiveData<List<MovieEntry>> loadAllMovieEntry();

    @Insert
    void insertTask(MovieEntry movieEntry);

    @Delete
    void deleteTask(MovieEntry movieEntry);

    @Query("SELECT * FROM movie WHERE code = :code")
    LiveData<MovieEntry> loadMovieEntryById(int code);
}
