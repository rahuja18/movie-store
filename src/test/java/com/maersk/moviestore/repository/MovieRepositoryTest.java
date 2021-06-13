package com.maersk.moviestore.repository;

import com.maersk.moviestore.model.Movie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MovieRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void test_Successful_FindById() {
        Movie movie = createMovie();
        testEntityManager.persist(movie);
        testEntityManager.flush();

        Optional<Movie> foundMovie = movieRepository.findById(movie.getName());

        assertThat(foundMovie.get())
                .isEqualTo(movie);
    }

    @Test
    public void test_Successful_FindByYear_WhenMovieExist() {
        Movie movie = createMovie();
        testEntityManager.persist(movie);
        testEntityManager.flush();

        List<Movie> movieList = movieRepository.findByYear(movie.getYear());

        assertThat(movieList).hasOnlyOneElementSatisfying(actualMovie -> {
            assertThat(actualMovie.getName()).isEqualTo(movie.getName());
            assertThat(actualMovie.getYear()).isEqualTo(movie.getYear());
            assertThat(actualMovie.getRating()).isEqualTo(movie.getRating());
        });
    }

    @Test
    public void test_Successful_FindByYear_WhenNoMovieExist() {
        Movie movie = createMovie();
        testEntityManager.persist(movie);
        testEntityManager.flush();

        List<Movie> movieList = movieRepository.findByYear(2301);

        assertThat(movieList).isEqualTo(Collections.EMPTY_LIST);
    }

    @Test
    public void test_Successful_FindByRating_WhenMovieExist() {
        Movie movie = createMovie();
        testEntityManager.persist(movie);
        testEntityManager.flush();

        List<Movie> movieList = movieRepository.findByRating(movie.getRating());

        assertThat(movieList).hasOnlyOneElementSatisfying(actualMovie -> {
            assertThat(actualMovie.getName()).isEqualTo(movie.getName());
            assertThat(actualMovie.getYear()).isEqualTo(movie.getYear());
            assertThat(actualMovie.getRating()).isEqualTo(movie.getRating());
        });
    }

    @Test
    public void test_Successful_FindByRating_WhenNoMovieExist() {
        Movie movie = createMovie();
        testEntityManager.persist(movie);
        testEntityManager.flush();

        List<Movie> movieList = movieRepository.findByRating("any");

        assertThat(movieList).isEqualTo(Collections.EMPTY_LIST);
    }

    public static Movie createMovie() {
        return new Movie("Rocky", 2002, "A");
    }
}
