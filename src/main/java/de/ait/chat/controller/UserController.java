package de.ait.chat.controller;

import de.ait.chat.entity.User;
import de.ait.chat.entity.dto.UserDTO;
import de.ait.chat.service.ConfirmationService;
import de.ait.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users") // Базовый URL
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ConfirmationService confirmationService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class); // Логгер для контроллера

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        try {
            UserDTO savedUser = userService.register(userDTO);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка при регистрации пользователя: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new UserDTO());
        }
    }

    // Активация пользователя
    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam("code") String code) {
        try {
            confirmationService.activateUser(code);
            return ResponseEntity.ok("User account activated successfully");
        } catch (Exception e) {
            logger.error("Ошибка активации пользователя с кодом: {}", code, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid activation code");
        }
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
        // Проверяем, переданы ли параметры
        if (firstName == null && lastName == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // Вызываем метод поиска
        List<UserDTO> users = userService.findUserByCriteria(firstName, lastName);

        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Поиск пользователя по ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Если пользователь не найден
        }
        return ResponseEntity.ok(user);
    }

    // Поиск пользователя по email
    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Если пользователь не найден
        }
        return ResponseEntity.ok(user); // Если пользователь найден
    }
}
