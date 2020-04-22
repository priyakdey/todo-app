package io.todo.api.controller;

import io.todo.api.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/users", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class UserDetailsController {

    @GetMapping(path = "/{username}")
    public ResponseEntity<String> getUserProfile(@PathVariable String username, HttpServletRequest request) {
        return ResponseEntity.ok("GET " + request.getRequestURI());
    }

    @PreAuthorize(value = "#username == authentication.name")
    @PutMapping(path = "/{username}")
    public ResponseEntity<String> updateUserProfile(@PathVariable String username, HttpServletRequest request, @RequestBody User user) {
        return ResponseEntity.ok("PUT " + request.getRequestURI());
    }
}
