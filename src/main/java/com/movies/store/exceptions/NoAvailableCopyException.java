package com.movies.store.exceptions;

public class NoAvailableCopyException extends RuntimeException {

    public NoAvailableCopyException(String message) {
        super(message);
    }
}
