package de.ait.chat.service;

import de.ait.chat.entity.User;
import java.util.Optional;

public interface UserService {

    User register(User user);

    Optional<User> getUserById(Long id);

    User findByUsername(String username);

    User findByEmail(String email);
}
