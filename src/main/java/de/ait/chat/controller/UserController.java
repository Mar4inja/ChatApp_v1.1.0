package de.ait.chat.controller;

import de.ait.chat.entity.User;
import de.ait.chat.entity.dto.UserDTO;
import de.ait.chat.exceptions.UserNotFoundException;
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
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
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

    @GetMapping("/findUser")
    public ResponseEntity<List<UserDTO>> findUserByCriteria(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {

        logger.debug("Получен запрос с параметрами: firstName={}, lastName={}", firstName, lastName);

        // Izsaukt servisa metodi, kas veiks visu meklēšanas loģiku
        List<UserDTO> users = userService.findUserByCriteria(firstName, lastName);

        // Ja lietotāji nav atrasti, atgriežam 404 statusu
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Ja lietotāji ir atrasti, atgriežam tos ar 200 statusu
        return new ResponseEntity<>(users, HttpStatus.OK);
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
