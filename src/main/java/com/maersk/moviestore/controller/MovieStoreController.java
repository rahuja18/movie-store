package com.maersk.moviestore.controller;

import com.maersk.moviestore.exception.IncorrectMovieException;
import com.maersk.moviestore.model.Movie;
import com.maersk.moviestore.service.MovieStoreService;
import com.maersk.moviestore.vo.MovieResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class MovieStoreController {

    private static final Logger LOG = LoggerFactory.getLogger(MovieStoreController.class);

    @Autowired
    private MovieStoreService movieStoreService;


    /** REST API to create new movie. When movie already exists it  will throw IncorrectMovieException
     *
     * **/
    @PostMapping(value = "/movie", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    private ResponseEntity<Movie> createMovieStore(@Valid @RequestBody Movie movie) throws IncorrectMovieException {
        LOG.info("Received request to create movie: " + movie);
        Movie responseMovie = movieStoreService.persistMovie(movie);
        LOG.info("Response of create movie: " + responseMovie);
        return new ResponseEntity<Movie>(responseMovie, CREATED);
    }

    @PutMapping(value = "/movie/{name}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    private ResponseEntity<Movie> updateMovieStore(@PathVariable("name") String name, @Valid @RequestBody Movie movie) throws IncorrectMovieException {
        LOG.info("Received request to update movie with name: " + name);
        Movie responseMovie = movieStoreService.updateMovie(name, movie);
        LOG.info("Response of update movie: " + responseMovie);
        return ok().body(responseMovie);
    }

    @GetMapping(value = "/movies", produces = APPLICATION_JSON_VALUE)
    private ResponseEntity<MovieResult> getAllMovies() {
        LOG.info("Received request to get all movies");
        MovieResult movieResult = movieStoreService.getAllMovies();
        LOG.info("Response of get all movies: " + movieResult);
        return ok().body(movieResult);
    }

    @GetMapping(value = "/movies", params = {"year"}, produces = APPLICATION_JSON_VALUE)
    private ResponseEntity<MovieResult> getAllMoviesByYear( @RequestParam("year") Integer year) {
        if (year == null || year.toString().length() != 4 || year.intValue() < 0) {
            throw new IllegalArgumentException("Invalid value passed for year. Year must be 4 digit positive number");
        }
        LOG.info("Received request to get all movies for a given year: " + year);
        MovieResult movieResult = movieStoreService.fetchMoviesByYear(year);
        LOG.info("Response of get all movies by year: " + movieResult);
        return ok().body(movieResult);
    }

    @GetMapping(value = "/movies", params = {"rating"}, produces = APPLICATION_JSON_VALUE)
    private ResponseEntity<MovieResult> getAllMoviesByRating(@RequestParam("rating") String rating) {
        LOG.info("Received request to get all movies with given rating: " + rating);
        MovieResult movieResult = movieStoreService.fetchMoviesByRating(rating);
        LOG.info("Response of get all movies by rating: " + movieResult);
        return ok().body(movieResult);
    }
}
