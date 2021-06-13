package com.maersk.moviestore.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class IncorrectMovieExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(IncorrectMovieExceptionHandler.class);

    @ExceptionHandler(IncorrectMovieException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectMovieException(IncorrectMovieException incorrectMovieException){
        List<String> errorDetails = new ArrayList<>();
        errorDetails.add(incorrectMovieException.getLocalizedMessage());
        ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR.value(), "Incorrect Movie", errorDetails);
        LOG.error("Incorrect Movie Exception occured:", incorrectMovieException);
        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception){
        List<String> errorDetails = new ArrayList<>();
        errorDetails.add(exception.getLocalizedMessage());
        ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR.value(), "Server Error", errorDetails);
        LOG.error("Exception occured:", exception);
        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        List<String> errorDetails = new ArrayList<>();
        for(ObjectError error : exception.getBindingResult().getAllErrors()){
            errorDetails.add(error.getDefaultMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST.value(), "Validation Failed", errorDetails);
        LOG.error("MethodArgumentNotValidException occured:", exception);
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }
}
