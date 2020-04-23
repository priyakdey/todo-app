package io.todo.api.exception;

public class UserAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = -2096422678057022208L;

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
