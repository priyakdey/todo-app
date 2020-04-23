package io.todo.api.exception.handler;

import io.todo.api.exception.UserAlreadyExistsException;
import io.todo.api.exception.UserNotFoundException;
import io.todo.api.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApplicationExceptionGlobalHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> userAlreadyExistsException(UserAlreadyExistsException e) {
        ErrorResponse errorResponse = generateResponse(e);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserAlreadyExistsException e) {
        ErrorResponse errorResponse = generateResponse(e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    private ErrorResponse generateResponse(Exception e) {
        return new ErrorResponse(e.getMessage());
    }
}
