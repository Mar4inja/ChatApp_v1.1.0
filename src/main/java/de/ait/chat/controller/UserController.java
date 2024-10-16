package de.ait.chat.controller;

import de.ait.chat.entity.User;
import de.ait.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users") // Базовый URL для API пользователей
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            User savedUser = userService.register(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<User> login(@RequestBody Map<String, String> credentials) {
//        String email = credentials.get("email");
//        String password = credentials.get("password");
//
//        try {
//            User user = userService.login(email, password);
//            return ResponseEntity.ok(user); // Atgriežam statusu 200 (Uzsākt)
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(null); // Atgriežam statusu 400 (Nederīgs pieprasījums)
//        }
    }

}
