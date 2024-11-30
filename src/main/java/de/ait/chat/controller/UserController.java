package de.ait.chat.controller;

import de.ait.chat.entity.User;
import de.ait.chat.entity.dto.UserDTO;
import de.ait.chat.service.ConfirmationService;
import de.ait.chat.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    // Reģistrācijas metode
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        try {
            UserDTO savedUser = userService.register(userDTO);
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
    public ResponseEntity<UserDTO> updateUser(Authentication authentication, @RequestBody UserDTO updatedUserDto) {
        userService.updateData(authentication, updatedUserDto);
        return ResponseEntity.ok(userService.getUserInfo(authentication));
    }

    @GetMapping("/auth/me")
    public ResponseEntity<UserDTO> getUserInfo(Authentication authentication) {
        UserDTO userInfo = userService.getUserInfo(authentication);
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {

        List<UserDTO> users = userService.findUsers(firstName, lastName);

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok(users); // 200 OK
    }


    // Lietotāju meklēšana pēc firstName
    @GetMapping("/search/firstName")
    public ResponseEntity<List<UserDTO>> searchUsersByFirstName(@RequestParam(required = false) String firstName) {
        log.info("Searching for users by firstName={}", firstName);
        List<UserDTO> users = userService.findByFirstName(firstName);

        if (users.isEmpty()) {
            log.info("No users found with the given firstName");
            return ResponseEntity.noContent().build(); // Возвращает 204 No Content
        }
        return ResponseEntity.ok(users);
    }

    // Lietotāju meklēšana pēc lastName
    @GetMapping("/search/by-lastName")
    public ResponseEntity<List<UserDTO>> searchUsersByLastName(@RequestParam(required = false) String lastName) {
        log.info("Searching for users by lastName={}", lastName);
        List<UserDTO> users = userService.findByLastName(lastName);

        if (users.isEmpty()) {
            log.info("No users found with the given lastName");
            return ResponseEntity.noContent().build(); // Возвращает 204 No Content
        }
        return ResponseEntity.ok(users);
    }


    // Lietotāja atrašana pēc ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    // Lietotāja atrašana pēc e-pasta
    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Atgriež 404
        }
        return ResponseEntity.ok(user); // Atgriež lietotāju
    }
}
