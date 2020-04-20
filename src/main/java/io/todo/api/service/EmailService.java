package io.todo.api.service;

import javax.mail.MessagingException;

public interface EmailService {

    /**
     * Method signature to send emails to users
     * @param recipient
     * @param username
     * @param name
     */
    void sendNotification(String recipient, String username, String token, String name) throws MessagingException;

}
