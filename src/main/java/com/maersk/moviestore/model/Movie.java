package com.maersk.moviestore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.maersk.moviestore.validator.ValidateYear;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "movie_store")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    @Id
    @NotBlank(message = "Movie name cannot be blank")
    private String name;

    @ValidateYear
    private Integer year;

    private String rating;

    public Movie() {
    }

    public Movie( String name, int year, String rating) {
        this.name = name;
        this.year = year;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", rating='" + rating + '\'' +
                '}';
    }
}
