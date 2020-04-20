package io.todo.api.controller;

import io.todo.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/verify")
public class AccountVerificationController {
    private final UserService userService;

    @Autowired
    public AccountVerificationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public void verifyAndEnableAccount(@RequestParam("username") String username, @RequestParam("token") String token) {
        userService.enableAccount(username, token);
    }
}
