package de.ait.chat.service;

import de.ait.chat.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    User register(User user);

    User updateData(Authentication authentication, User updatedUser);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findUsers(String firstName, String lastName);

    User findById(Long id);

    User getUserInfo(Authentication authentication);
}
