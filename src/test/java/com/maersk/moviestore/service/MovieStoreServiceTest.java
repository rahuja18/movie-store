package com.maersk.moviestore.service;

import com.maersk.moviestore.exception.IncorrectMovieException;
import com.maersk.moviestore.model.Movie;
import com.maersk.moviestore.repository.MovieRepository;
import com.maersk.moviestore.vo.MovieResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MovieStoreServiceTest {

    @Mock
    MovieRepository movieRepository;

    @Autowired
    private MovieStoreService movieStoreService;

    @Before
    public void setUp() {
        movieStoreService = new MovieStoreService(movieRepository);
    }

    @Test
    public void test_Successful_PersistMovie() {
        Movie movie = createMovie();

        when(movieRepository.findById(any(String.class))).thenReturn(empty());
        when(movieRepository.save(movie)).thenReturn(movie);
        Movie persistedMovie = movieStoreService.persistMovie(movie);
        Assert.assertEquals(movie, persistedMovie);
    }

    @Test(expected = IncorrectMovieException.class)
    public void shouldThrow_IncorrectMovieException_WhenMovieAlreadyExists_In_PersistMovie() throws IncorrectMovieException {
        Movie movie = createMovie();

        when(movieRepository.findById(any(String.class))).thenReturn(Optional.of(movie));
        movieStoreService.persistMovie(movie);
    }

    @Test
    public void test_Successful_UpdateMovie() {
        Movie existingMovie = createMovie();
        Movie newMovie = createMovie();

        when(movieRepository.findById(existingMovie.getName())).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(existingMovie)).thenReturn(newMovie);
        Movie updatedMovie = movieStoreService.updateMovie(existingMovie.getName(), newMovie);
        Assert.assertEquals(newMovie, updatedMovie);
    }

    @Test(expected = IncorrectMovieException.class)
    public void shouldThrow_IncorrectMovieException_WhenMovieDoesNotExist_In_UpdateMovie() throws IncorrectMovieException {
        Movie movie = createMovie();

        when(movieRepository.findById(any(String.class))).thenReturn(empty());
        movieStoreService.updateMovie(movie.getName(), movie);
    }

    @Test
    public void test_Successful_GetAllMovies() {
        Movie movie = createMovie();
        List<Movie> movieList = new ArrayList<>();
        movieList.add(movie);
        MovieResult movieResult = new MovieResult();
        movieResult.setMovies(movieList);

        when(movieRepository.findAll()).thenReturn(movieList);
        MovieResult result = movieStoreService.getAllMovies();
        Assert.assertEquals(movieResult, result);
    }

    @Test
    public void test_Successful_FetchMoviesByYear() {
        Movie movie = createMovie();
        List<Movie> movieList = new ArrayList<>();
        movieList.add(movie);
        MovieResult movieResult = new MovieResult();
        movieResult.setMovies(movieList);

        when(movieRepository.findByYear(movie.getYear())).thenReturn(movieList);
        MovieResult result = movieStoreService.fetchMoviesByYear(movie.getYear());
        Assert.assertEquals(movieResult, result);
    }

    @Test(expected = IncorrectMovieException.class)
    public void shouldThrow_IncorrectMovieException_WhenMovieDoesNotExist_In_FetchMoviesByYear() throws IncorrectMovieException {
        Movie movie = createMovie();

        when(movieRepository.findByYear(any(Integer.class))).thenReturn(null);
        movieStoreService.fetchMoviesByYear(movie.getYear());
    }

    @Test
    public void test_Successful_FetchMoviesByRating() {
        Movie movie = createMovie();
        List<Movie> movieList = new ArrayList<>();
        movieList.add(movie);
        MovieResult movieResult = new MovieResult();
        movieResult.setMovies(movieList);

        when(movieRepository.findByRating(movie.getRating())).thenReturn(movieList);
        MovieResult result = movieStoreService.fetchMoviesByRating(movie.getRating());
        Assert.assertEquals(movieResult, result);
    }

    @Test(expected = IncorrectMovieException.class)
    public void shouldThrow_IncorrectMovieException_WhenMovieDoesNotExist_In_FetchMoviesByRating() throws IncorrectMovieException {
        Movie movie = createMovie();

        when(movieRepository.findByRating(any(String.class))).thenReturn(null);
        movieStoreService.fetchMoviesByRating(movie.getRating());
    }

    public static Movie createMovie() {
        return new Movie("Rocky", 2002, "A");
    }

}
