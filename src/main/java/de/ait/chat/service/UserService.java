package de.ait.chat.service;

import de.ait.chat.entity.User;

import java.util.List;

public interface UserService {

    User register(User user);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findUsers(String firstName, String lastName);

    User findById(Long id);
}
