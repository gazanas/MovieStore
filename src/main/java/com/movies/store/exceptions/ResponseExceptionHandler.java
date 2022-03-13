package com.movies.store.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class ResponseExceptionHandler {

    private final ExceptionUtil exceptionUtil;

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMovieNotFoundException(MovieNotFoundException e) {
        return this.exceptionUtil.assembleResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InformationNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleInformationNotFoundException(InformationNotFoundException e) {
        return this.exceptionUtil.assembleResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(CopyNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCopyNotFoundException(CopyNotFoundException e) {
        return this.exceptionUtil.assembleResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(NoAvailableCopyException.class)
    public ResponseEntity<Map<String, String>> handleNoAvailableCopyException(NoAvailableCopyException e) {
        return this.exceptionUtil.assembleResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(ReturnCopyException.class)
    public ResponseEntity<Map<String, String>> handleReturnCopyException(ReturnCopyException e) {
        return this.exceptionUtil.assembleResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return this.exceptionUtil.assembleResponse(HttpStatus.INTERNAL_SERVER_ERROR, errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errors = e.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return this.exceptionUtil.assembleResponse(HttpStatus.INTERNAL_SERVER_ERROR, errors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException e) {
        return this.exceptionUtil.assembleResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, String>> handleInvalidTokenException(InvalidTokenException e) {
        return this.exceptionUtil.assembleResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(NullTokenException.class)
    public ResponseEntity<Map<String, String>> handleNullTokenException(NullTokenException e) {
        return this.exceptionUtil.assembleResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
