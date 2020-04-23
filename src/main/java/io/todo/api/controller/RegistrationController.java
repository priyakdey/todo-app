package io.todo.api.controller;

import io.todo.api.model.bo.UserDetailsBO;
import io.todo.api.model.request.NewUserRequestModel;
import io.todo.api.model.response.ErrorResponse;
import io.todo.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.net.URI;
import java.net.URISyntaxException;

import static io.todo.api.model.bo.UserDetailsBO.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/signup", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class RegistrationController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping
    public ResponseEntity<?> registerNewUser(@RequestBody NewUserRequestModel model){
        String password = encodePassword(model.getPassword());
        UserDetailsBO userDetailsBO = buildUserDetailsBO(model, password);

        try {
            userService.saveUserDetails(userDetailsBO);
            return ResponseEntity
                    .created(new URI("/users/" + model.getUsername()))
                    .build();
        } catch (URISyntaxException | MessagingException e) {
            e.printStackTrace();
            ErrorResponse errorResponse = new ErrorResponse("We are facing some issue. Please try again later or contact admin.");
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    private UserDetailsBO buildUserDetailsBO(NewUserRequestModel model, String password) {
        UserDetailsBO userDetailsBO = UserDetailsBOBuilder.getInstance(model.getUsername())
                                                    .setPassword(password)
                                                    .setName(model.getName())
                                                    .setEmail(model.getEmail())
                                                    .build();

        return userDetailsBO;
    }


    private String encodePassword(char[] password) {
        String encodedPassword = passwordEncoder.encode(new String(password));
        clearPassword(password);
        return encodedPassword;
    }


    private void clearPassword(char[] password) {
        for (int i = 0; i < password.length; i++) {
            password[i] = '#';
        }
    }
}
