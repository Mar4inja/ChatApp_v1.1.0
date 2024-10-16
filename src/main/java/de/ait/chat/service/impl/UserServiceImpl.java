package de.ait.chat.service.impl;

import de.ait.chat.entity.User;
import de.ait.chat.repository.RoleRepository;
import de.ait.chat.repository.UserRepository;
import de.ait.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
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
        user.setRegistrationDate(LocalDateTime.now());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(true);
        User savedUser = userRepository.save(user);
        logger.info("User with " + user.getEmail() + " successfully registered");
        return savedUser;
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
