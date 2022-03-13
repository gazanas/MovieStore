package com.movies.store.exceptions;

public class InformationNotFoundException extends RuntimeException {

    public InformationNotFoundException(String message) {
        super(message);
    }
}
