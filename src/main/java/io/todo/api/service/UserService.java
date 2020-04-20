package io.todo.api.service;

import io.todo.api.model.bo.UserDetailsBO;

public interface UserService {

    /**
     * Method to process and save new user details
     * @param userDetailsBO
     */
    void saveUserDetails(UserDetailsBO userDetailsBO);
}
