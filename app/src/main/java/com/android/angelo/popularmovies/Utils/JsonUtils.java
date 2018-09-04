package com.android.angelo.popularmovies.Utils;

import com.android.angelo.popularmovies.Model.MovieListTO;
import com.android.angelo.popularmovies.Model.MovieTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonUtils {
    private static final String PAGE = "page";
    private static final String TOTOL_RESULTS = "total_results";
    private static final String TOTAL_PAGES = "total_pages";
    private static final String VOTE_COUNT = "vote_count";
    private static final String RESULTS = "results";
    private static final String ID = "id";
    private static final String VIDEO = "video";
    private static final String TITLE = "title";
    private static final String VOTE_AVARAGE = "vote_average";
    private static final String POPULARITY = "popularity";
    private static final String POSTER_PATH = "poster_path";
    private static final String ORIGINAL_LANGUAGE = "original_language";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String GENRE_IDS = "genre_ids";
    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String ADULT = "adult";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";



    public static MovieListTO parseJsonToMovieListTO(JSONObject json) throws JSONException, ParseException {
        MovieListTO movieListTO = new MovieListTO();
        if(json != null){
            movieListTO.setPage_number(json.getInt(PAGE));
            movieListTO.setTotal_results(json.getInt(TOTOL_RESULTS));
            movieListTO.setTotal_pages(json.getInt(TOTAL_PAGES));
            JSONArray results = json.getJSONArray(RESULTS);
            List<MovieTO> movies = new ArrayList<>(results.length());
            for (int i = 0; i<results.length(); i++) {
                MovieTO movie = parseJsonToMovieTO(results.getJSONObject(i));
                movies.add(movie);
            }
            movieListTO.setMovies(movies);
        }
        return movieListTO;
    }


    public static MovieTO parseJsonToMovieTO(JSONObject json) throws JSONException, ParseException {
        MovieTO movieTO = new MovieTO();
        if(json != null){
            movieTO.setVote_count(json.getInt(VOTE_COUNT));
            movieTO.setId(json.getInt(ID));
            movieTO.setVideo(json.getBoolean(VIDEO));
            movieTO.setVote_avarage(json.getDouble(VOTE_AVARAGE));
            movieTO.setTitle(json.getString(TITLE));
            movieTO.setPopularity(json.getDouble(POPULARITY));
            movieTO.setPoster_path(json.getString(POSTER_PATH));
            movieTO.setOriginal_language(json.getString(ORIGINAL_LANGUAGE));
            movieTO.setOriginal_title(json.getString(ORIGINAL_TITLE));
            JSONArray genre = json.getJSONArray(GENRE_IDS);
            List<Integer> genre_ids = new ArrayList<>(genre.length());
            for (int i=0; i<genre.length(); i++){
                genre_ids.add(genre.getInt(i));
            }
            movieTO.setGenre_ids(genre_ids);
            movieTO.setBackdrop_path(json.getString(BACKDROP_PATH));
            movieTO.setAdult(json.getBoolean(ADULT));
            movieTO.setOverview(json.getString(OVERVIEW));
            String dateS = json.getString(RELEASE_DATE);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(dateS);
            movieTO.setRelease_date(date);
        }
        return movieTO;
    }


}
