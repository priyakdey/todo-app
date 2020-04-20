package io.todo.api.repository;

import io.todo.api.entity.User;

import java.util.Optional;

public interface UserRepository {

    /**
     * Method is to persit user credentials and user profile details
     * @param user
     */
    void persist(User user);


    /**
     * Method to check if an username/email is already present in the system
     * @param username
     * @param email
     * @return
     */
    boolean ifExists(String username, String email);

    /**
     * Method returns the User fetched by username
     * @param username
     * @return @{@link Optional<User>}
     */
    Optional<User> findById(String username);
}
