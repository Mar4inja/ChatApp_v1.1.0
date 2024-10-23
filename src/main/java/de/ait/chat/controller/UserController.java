package de.ait.chat.controller;

import de.ait.chat.entity.User;
import de.ait.chat.service.ConfirmationService;
import de.ait.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users") // Базовый URL для API пользователей
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ConfirmationService confirmationService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            User savedUser = userService.register(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam("code") String code) {
        confirmationService.activateUser(code);
        return ResponseEntity.ok("User account activated successfully");

    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {
        List<User> users = userService.findUsers(firstName, lastName);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}