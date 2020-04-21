package io.todo.api.service.impl;

import io.todo.api.entity.User;
import io.todo.api.entity.UserAuthority;
import io.todo.api.entity.UserProfile;
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
            // TODO: Custom exception: User Registered ex
            throw new RuntimeException("User already registered");

        User user = buildUser(userDetailsBO);
        user.getAuthorities().add(UserAuthority.USER_UPDATE);
        user.getAuthorities().add(UserAuthority.USER_READ);
        userRepository.persist(user);
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
                                           // TODO: Custom exception -> No user found exception
                                        () -> new RuntimeException("Username does not exists. This is a bad token")
                                    );
        boolean isValid = JwtUtils.validateToken(token, user.getVerificationToken(), jwtBean);

        if (isValid){
            user.setEnabled(Boolean.TRUE);
            user.setVerificationToken(null);
        } else {
            // TODO: Custom exception: Invalid token from email
            throw new RuntimeException("Invalid Token from email");
        }
    }
}
