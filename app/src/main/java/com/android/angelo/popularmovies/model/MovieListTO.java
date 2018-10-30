package com.android.angelo.popularmovies.model;

import java.util.List;

public class MovieListTO {
    private int page_number;
    private int total_results;
    private int total_pages;
    private List<MovieTO> movies;

    /** Getters And Setters Method**/

    public int getPage_number() {
        return page_number;
    }

    public void setPage_number(int page_number) {
        this.page_number = page_number;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<MovieTO> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieTO> movies) {
        this.movies = movies;
    }
}
