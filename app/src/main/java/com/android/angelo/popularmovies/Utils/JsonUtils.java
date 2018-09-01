package com.android.angelo.popularmovies.Utils;

import com.android.angelo.popularmovies.Model.MovieListTO;
import com.android.angelo.popularmovies.Model.MovieTO;

import org.json.JSONObject;

public class JsonUtils {

    public static MovieListTO parseJsonToMovieListTO(JSONObject json){
        MovieListTO movieListTO = new MovieListTO();
        return movieListTO;
    }


    public static MovieTO parseJsonToMovieTO(JSONObject json){
        MovieTO movieTO = new MovieTO();
        return movieTO;
    }


}
