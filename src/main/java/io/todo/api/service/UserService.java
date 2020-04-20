package io.todo.api.service;

import io.todo.api.model.bo.UserDetailsBO;

import javax.mail.MessagingException;

public interface UserService {

    /**
     * Method to process and save new user details
     * @param userDetailsBO
     */
    void saveUserDetails(UserDetailsBO userDetailsBO) throws MessagingException;


    /**
     * This method is to verify if the token is valid
     * If valid token received, user account to be enabled
     * If not, request should be rejected with an exception
     * @param username
     * @param token
     */
    void enableAccount(String username, String token);
}
