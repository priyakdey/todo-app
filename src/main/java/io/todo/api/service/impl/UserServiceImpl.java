package io.todo.api.service.impl;

import io.todo.api.entity.User;
import io.todo.api.entity.UserProfile;
import io.todo.api.model.bo.UserDetailsBO;
import io.todo.api.repository.UserRepository;
import io.todo.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void saveUserDetails(UserDetailsBO userDetailsBO) {
        if (!validateUserDetails(userDetailsBO))
            // TODO: Custom exception: Validation ex
            throw new RuntimeException("Invalid Inputs");

        if (userRepository.ifExists(userDetailsBO.getUsername(), userDetailsBO.getEmail()))
            // TODO: Custom exception: User Registered ex
            throw new RuntimeException("User already registered");

        User user = buildUser(userDetailsBO);
        userRepository.persist(user);
        // TODO: Send an email notification for verification
    }

    private User buildUser(UserDetailsBO userDetailsBO) {
        User user = new User();
        user.setUsername(userDetailsBO.getUsername());
        user.setPassword(userDetailsBO.getPassword());
        user.setVerificationToken(generateToken(userDetailsBO));

        UserProfile userProfile = new UserProfile();
        userProfile.setName(userDetailsBO.getName());
        userProfile.setEmail(userDetailsBO.getEmail());

        user.setUserProfile(userProfile);

        return user;
    }

    private String generateToken(UserDetailsBO userDetailsBO) {
        // TODO: Impl generate token method
        return "dummy-token";
    }


    private boolean validateUserDetails(UserDetailsBO userDetailsBO) {
        //TODO: Implement validation of the data
        return true;
    }
}
