package de.ait.chat.controller;

import de.ait.chat.entity.User;
import de.ait.chat.service.ConfirmationService;
import de.ait.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/users") // Bāzes URL
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ConfirmationService confirmationService;

    // Reģistrācijas metode
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            User savedUser = userService.register(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Lietotāja aktivācija
    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam("code") String code) {
        confirmationService.activateUser(code);
        return ResponseEntity.ok("User account activated successfully");
    }

    @PutMapping("/auth/me")
    public ResponseEntity<User> updateUser(Authentication authentication, @RequestBody User updatedUser) {
        userService.updateData(authentication, updatedUser);
        return ResponseEntity.ok(userService.getUserInfo(authentication));
    }


    // Lietotāju meklēšana
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam(required = false) String firstName,
                                                  @RequestParam(required = false) String lastName) {
        List<User> users = userService.findUsers(firstName, lastName);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Lietotāja atrašana pēc ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    // Lietotāja atrašana pēc e-pasta
    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Atgriež 404, ja lietotājs nav atrasts
        }
        return ResponseEntity.ok(user); // Atgriež lietotāju
    }
}
