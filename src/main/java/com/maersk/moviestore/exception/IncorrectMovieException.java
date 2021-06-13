package com.maersk.moviestore.exception;

public class IncorrectMovieException extends RuntimeException{

    private final String message;

    public IncorrectMovieException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
