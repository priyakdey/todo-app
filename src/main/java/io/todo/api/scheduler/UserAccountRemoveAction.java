package io.todo.api.scheduler;

import io.todo.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UserAccountRemoveAction {
    private final UserRepository userRepository;

    @Autowired
    public UserAccountRemoveAction(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Action to delete any user details who haven't verified their account in 24 hours
     */
    @Scheduled(initialDelay = 1000, fixedRate = 1000)
    public void deleteUserProfile() {
        // TODO: to be implemented
    }
}
