
package com.maersk.moviestore.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maersk.moviestore.exception.IncorrectMovieException;
import com.maersk.moviestore.model.Movie;
import com.maersk.moviestore.service.MovieStoreService;
import com.maersk.moviestore.vo.MovieResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class MovieStoreControllerTest {

    @Mock
    MovieStoreController movieStoreController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieStoreService movieStoreService;

    @Test
    public void test_Successful_CreateMovie_WithValidRequest() throws Exception {

        Movie movie = createMovie();

        when(movieStoreService.persistMovie(any(Movie.class))).thenReturn(movie);

        RequestBuilder requestBuilder;
        requestBuilder = MockMvcRequestBuilders
                .post("/movie")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonStringFrom(movie));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(movie.getName()))
                .andExpect(jsonPath("$.year").value(movie.getYear()))
                .andExpect(jsonPath("$.rating").value(movie.getRating()));
    }

    @Test
    public void test_IncorrectMovieException_WhenMovieAlreadyExist_CreateMovie() throws Exception {

        Movie movie = createMovie();
        String exceptionDetails = "Incorrect movie in test";
        when(movieStoreService.persistMovie(any(Movie.class))).thenThrow(new IncorrectMovieException(exceptionDetails));

        RequestBuilder requestBuilder;
        requestBuilder = MockMvcRequestBuilders
                .post("/movie")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonStringFrom(movie));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Incorrect Movie"))
                .andExpect(jsonPath("$.details[0]").value(exceptionDetails));
    }

   @Test
    public void test_ValidationErrors_When_MoveNameIsNull_CreateMovie() throws Exception {

        Movie movie = new Movie(null, 2002, "A");

        String exceptionDetails = "Movie name cannot be blank";

        RequestBuilder requestBuilder;
        requestBuilder = MockMvcRequestBuilders
                .post("/movie")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonStringFrom(movie));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.details[0]").value(exceptionDetails));
    }

    @Test
    public void test_ValidationErrors_When_YearIsInValid_CreateMovie() throws Exception {

        Movie movie = new Movie("Rocky", 200200, "A");

        String exceptionDetails = "Year must be 4 digits positive number";

        RequestBuilder requestBuilder;
        requestBuilder = MockMvcRequestBuilders
                .post("/movie")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonStringFrom(movie));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.details[0]").value(exceptionDetails));
    }

    @Test
    public void test_UpdateMovie_WithValidRequest() throws Exception {

        Movie movie = createMovie();

        when(movieStoreService.updateMovie(eq(movie.getName()), any(Movie.class))).thenReturn(movie);

        RequestBuilder requestBuilder;
        requestBuilder = MockMvcRequestBuilders
                .put("/movie/{name}", movie.getName())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonStringFrom(movie));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(movie.getName()))
                .andExpect(jsonPath("$.year").value(movie.getYear()))
                .andExpect(jsonPath("$.rating").value(movie.getRating()));
    }


    @Test
    public void test_Successful_GetAllMovies() throws Exception {

        Movie movie = createMovie();
        List<Movie> movieList = new ArrayList<>();
        movieList.add(movie);
        MovieResult movieResult = new MovieResult();
        movieResult.setMovies(movieList);
        when(movieStoreService.getAllMovies()).thenReturn(movieResult);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/movies")
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movies", hasSize(1)))
                .andExpect(jsonPath("$.movies[0].name").value(movie.getName()))
                .andExpect(jsonPath("$.movies[0].year").value(movie.getYear()))
                .andExpect(jsonPath("$.movies[0].rating").value(movie.getRating()));
    }

    @Test
    public void test_Successful_GetAllMoviesByYear() throws Exception {

        Movie movie = createMovie();
        List<Movie> movieList = new ArrayList<>();
        movieList.add(movie);
        MovieResult movieResult = new MovieResult();
        movieResult.setMovies(movieList);
        when(movieStoreService.fetchMoviesByYear(movie.getYear())).thenReturn(movieResult);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/movies?year={year}", movie.getYear())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movies", hasSize(1)))
                .andExpect(jsonPath("$.movies[0].name").value(movie.getName()))
                .andExpect(jsonPath("$.movies[0].year").value(movie.getYear()))
                .andExpect(jsonPath("$.movies[0].rating").value(movie.getRating()));
    }

    @Test
    public void test_ValidationError_WhenInvalidYearIsPassed_GetAllMoviesByYear() throws Exception {

        String exceptionDetails = "Invalid value passed for year. Year must be 4 digit positive number";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/movies?year={year}", 10101010)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Server Error"))
                .andExpect(jsonPath("$.details[0]").value(exceptionDetails));
    }

    @Test
    public void test_ValidationError_WhenNullYearIsPassed_GetAllMoviesByYear() throws Exception {

        String exceptionDetails = "Invalid value passed for year. Year must be 4 digit positive number";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/movies?year=")
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Server Error"))
                .andExpect(jsonPath("$.details[0]").value(exceptionDetails));
    }

    @Test
    public void test_ValidationError_WhenNegativeYearIsPassed_GetAllMoviesByYear() throws Exception {

        String exceptionDetails = "Invalid value passed for year. Year must be 4 digit positive number";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/movies?year={year}", -2002)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Server Error"))
                .andExpect(jsonPath("$.details[0]").value(exceptionDetails));
    }

    @Test
    public void test_Successful_GetAllMoviesByRating() throws Exception {

        Movie movie = createMovie();
        List<Movie> movieList = new ArrayList<>();
        movieList.add(movie);
        MovieResult movieResult = new MovieResult();
        movieResult.setMovies(movieList);
        when(movieStoreService.fetchMoviesByRating(movie.getRating())).thenReturn(movieResult);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/movies?rating={rating}", movie.getRating())
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movies", hasSize(1)))
                .andExpect(jsonPath("$.movies[0].name").value(movie.getName()))
                .andExpect(jsonPath("$.movies[0].year").value(movie.getYear()))
                .andExpect(jsonPath("$.movies[0].rating").value(movie.getRating()));
    }

    @Test
    public void test_ValidationError_WhenNullRatingIsPassed_GetAllMoviesByRating() throws Exception {

        String exceptionDetails = "No movies found with the given rating: ";
        when(movieStoreService.fetchMoviesByRating("")).thenThrow(new IncorrectMovieException(exceptionDetails));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/movies?rating=")
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Incorrect Movie"))
                .andExpect(jsonPath("$.details[0]").value(exceptionDetails));
    }

    public static String jsonStringFrom(Object value) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(value);
    }

    public static Movie createMovie() {
        return new Movie("Rocky", 2002, "A");
    }

}
