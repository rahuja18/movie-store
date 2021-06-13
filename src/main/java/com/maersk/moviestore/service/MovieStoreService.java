package com.maersk.moviestore.service;

import com.maersk.moviestore.exception.IncorrectMovieException;
import com.maersk.moviestore.model.Movie;
import com.maersk.moviestore.repository.MovieRepository;
import com.maersk.moviestore.vo.MovieResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieStoreService {

    public static final Logger LOG = LoggerFactory.getLogger(MovieStoreService.class);

    private MovieRepository movieRepository;

    @Autowired
    public MovieStoreService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie persistMovie(Movie movie) throws IncorrectMovieException {

        Optional<Movie> existingMovie = movieRepository.findById(movie.getName());
        if (existingMovie.isPresent()) {
            LOG.info("Found existing movie with details: " + existingMovie);
            throw new IncorrectMovieException("Given movie name already exists. Cannot create movie: " + movie.getName());
        }

        movieRepository.save(movie);
        return movie;
    }

    public Movie updateMovie(String name, Movie movie) throws IncorrectMovieException {

        movieRepository.findById(name)
                .map(existingMovie -> {
                    LOG.info("Found existing movie with details: " + existingMovie);
                    existingMovie.setYear(movie.getYear());
                    existingMovie.setRating(movie.getRating());
                    LOG.info("Updating existing movie with year: "+movie.getYear());
                    LOG.info("Updating existing movie with rating: "+movie.getRating());
                    return movieRepository.save(existingMovie);
                }).orElseThrow(() -> new IncorrectMovieException("Given movie name " + name + " does not exists. Cannot update movie"));
        movie.setName(name);
        return movie;
    }

    public MovieResult getAllMovies() {
        return new MovieResult(movieRepository.findAll());
    }

    public MovieResult fetchMoviesByYear(int year) throws IncorrectMovieException {

        List<Movie> movieList = Optional.ofNullable(movieRepository.findByYear(year))
                .orElseThrow(() -> new IncorrectMovieException("No movies found with the given year: "+year));
        return new MovieResult(movieList);
    }

    public MovieResult fetchMoviesByRating(String rating) throws IncorrectMovieException {
        List<Movie> movieList = Optional.ofNullable(movieRepository.findByRating(rating))
                .orElseThrow(() -> new IncorrectMovieException("No movies found with the given rating: " +rating));
        return new MovieResult(movieList);
    }
}
