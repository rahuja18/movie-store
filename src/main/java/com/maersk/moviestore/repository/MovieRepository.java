package com.maersk.moviestore.repository;

import com.maersk.moviestore.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {

    List<Movie> findByYear(int year);

    List<Movie> findByRating(String rating);

}
