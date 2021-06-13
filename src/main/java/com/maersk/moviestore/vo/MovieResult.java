package com.maersk.moviestore.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maersk.moviestore.model.Movie;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieResult {

    private List<Movie> movies;

    public MovieResult() {
    }

    public MovieResult(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieResult that = (MovieResult) o;
        return Objects.equals(movies, that.movies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movies);
    }
}
