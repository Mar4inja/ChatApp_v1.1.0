package de.ait.chat.service.impl;

import de.ait.chat.entity.User;
import de.ait.chat.repository.RoleRepository;
import de.ait.chat.repository.UserRepository;
import de.ait.chat.service.EmailService;
import de.ait.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;
    private final EmailService emailService;


    @Override
    public User register(User user) {
        user.setId(null);
        if (user.getFirstName() == null || user.getFirstName().isEmpty()
                || user.getLastName() == null || user.getLastName().isEmpty()
                || user.getBirthdate() == null
                || user.getEmail() == null || user.getEmail().isEmpty()
                || user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("All fields are required");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email address already in use");
        }
        user.setRoles(Collections.singleton(roleRepository.findByTitle("ROLE_USER")));
        validatePassword(user.getPassword());
        user.setRegistrationDate(LocalDateTime.now());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(false);
        User savedUser = userRepository.save(user);
        emailService.sendConfirmationEmail(user);
        logger.info("User with " + user.getEmail() + " successfully registered");
        return savedUser;
    }


    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one letter");
        }
        if (!password.matches(".*[.,?!@#$%^&+=].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character (.,?!@#$%^&+=)");
        }
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByEmail(username); // Assuming email is used as the username
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
