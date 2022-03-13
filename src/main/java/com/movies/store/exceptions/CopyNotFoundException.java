package com.movies.store.exceptions;

public class CopyNotFoundException extends RuntimeException {

    public CopyNotFoundException(String message) {
        super(message);
    }
}
