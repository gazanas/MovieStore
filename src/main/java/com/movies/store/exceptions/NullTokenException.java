package com.movies.store.exceptions;

public class NullTokenException extends RuntimeException {

    public NullTokenException(String message) {
        super(message);
    }
}
