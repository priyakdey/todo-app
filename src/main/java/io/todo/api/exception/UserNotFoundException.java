package io.todo.api.exception;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -7125116938791537397L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
