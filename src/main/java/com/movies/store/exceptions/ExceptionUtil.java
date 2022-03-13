package com.movies.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExceptionUtil {

    /**
     * Assemble the exception http response
     * It will contain the reason, the http status and the timestamp when it occurred
     *
     * @param status The http status of the exception
     * @param message The cause of the exception
     * @return The response entity to be sent to the user
     */
    public Map<String, String> assembleResponseBody(HttpStatus status, String message) {
        Map<String, String> body = new HashMap<>();
        body.put("status", status.toString());
        body.put("message", message);
        body.put("timestamp", LocalDateTime.now().toString());

        return body;
    }

    public Map<String, String> assembleResponseBody(HttpStatus status, List<String> messages) {
        Map<String, String> body = new HashMap<>();
        body.put("status", status.toString());
        body.put("message", messages.toString());
        body.put("timestamp", LocalDateTime.now().toString());

        return body;
    }

    public ResponseEntity<Map<String, String>> assembleResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(this.assembleResponseBody(status, message));
    }

    public ResponseEntity<Map<String, String>> assembleResponse(HttpStatus status, List<String> messages) {
        return ResponseEntity.status(status).body(this.assembleResponseBody(status, messages));
    }
}
