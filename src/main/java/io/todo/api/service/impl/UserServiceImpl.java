package io.todo.api.service.impl;

import io.todo.api.entity.User;
import io.todo.api.entity.UserAuthority;
import io.todo.api.entity.UserProfile;
import io.todo.api.exception.UserAlreadyExistsException;
import io.todo.api.exception.UserNotFoundException;
import io.todo.api.model.bo.UserDetailsBO;
import io.todo.api.repository.UserRepository;
import io.todo.api.security.jwt.JwtBean;
import io.todo.api.security.jwt.JwtUtils;
import io.todo.api.service.EmailService;
import io.todo.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

import static io.todo.api.security.jwt.JwtUtils.validateToken;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final JwtBean jwtBean;

    @Autowired
    public UserServiceImpl(EmailService emailService, UserRepository userRepository, JwtBean jwtBean) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.jwtBean = jwtBean;
    }


    @Override
    public void saveUserDetails(UserDetailsBO userDetailsBO) throws MessagingException {
        if (!validateUserDetails(userDetailsBO))
            // TODO: Custom exception: Validation ex
            throw new RuntimeException("Invalid Inputs");

        if (userRepository.ifExists(userDetailsBO.getUsername(), userDetailsBO.getEmail()))
            throw new UserAlreadyExistsException("User already registered");

        User user = buildUser(userDetailsBO);
        user.getAuthorities().add(UserAuthority.USER_UPDATE);
        user.getAuthorities().add(UserAuthority.USER_READ);
        userRepository.persist(user);

        /**
         * TODO: Should this email notification be send on a different thread / async process ?
         *
         * If async, how do we persist the data temporarily ?
         * If temporary, do we use Redis key-value / blocking queue holding the data ?
         * (In case of BQ the size needs to be huge)
         *
         * In case of async, when does the entry render bad, since user cannot sign up with same email id (24 hours ?)
         * TODO: Learn REDIS.
         */
        emailService.sendNotification(user.getUserProfile().getEmail(),
                                        user.getUsername(),
                                        user.getVerificationToken(),
                                        user.getUserProfile().getName());
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
        return JwtUtils.generateToken(userDetailsBO.getUsername(), jwtBean);
    }


    private boolean validateUserDetails(UserDetailsBO userDetailsBO) {
        //TODO: Implement validation of the data
        return true;
    }


    @Override
    public void enableAccount(String username, String token) {
        User user = userRepository.findById(username)
                                   .orElseThrow(
                                           // This scenario might never occur,
                                           // token will be only sent to email which have registered
                                        () -> new UserNotFoundException("Username does not exists. This is a bad token")
                                    );
        // First validation is to ignore any further requests from an already verified account
        boolean isValid = user.getVerificationToken() != null && validateToken(token, user.getVerificationToken(), jwtBean);

        if (isValid){
            user.setEnabled(Boolean.TRUE);
            user.setVerificationToken(null);
        } else {
            // TODO: Custom exception: Invalid token from email
            // This scenario will also not occur unless token is tampered and request sent manually
            throw new RuntimeException("Invalid Token from email");
        }
    }
}
