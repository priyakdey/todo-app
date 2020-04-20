package io.todo.api.service;

import io.todo.api.model.bo.UserDetailsBO;

import javax.mail.MessagingException;

public interface UserService {

    /**
     * Method to process and save new user details
     * @param userDetailsBO
     */
    void saveUserDetails(UserDetailsBO userDetailsBO) throws MessagingException;

}
