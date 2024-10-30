package org.pogonin.crud.web.controller;

import org.pogonin.crud.core.exceptions.SaveTaskException;
import org.pogonin.crud.core.exceptions.TaskAlreadyExistException;
import org.pogonin.crud.core.exceptions.TaskNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({SaveTaskException.class, TaskNotFoundException.class, TaskAlreadyExistException.class})
    public ResponseEntity<ExceptionMessage> handleTaskException(Exception ex) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionMessage(ex.getMessage(), 400, LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessage> handleOtherException(Exception ex) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionMessage(ex.getMessage(), 400, LocalDateTime.now()));
    }

    public record ExceptionMessage (String message, int statusCode, LocalDateTime timestamp) {}
}
