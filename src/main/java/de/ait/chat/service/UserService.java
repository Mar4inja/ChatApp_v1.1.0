package de.ait.chat.service;


import de.ait.chat.entity.User;
import de.ait.chat.entity.dto.UserDTO;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;


public interface UserService {

    UserDTO register(UserDTO userDTO);

    UserDTO updateData(Authentication authentication, UserDTO updatedUserDTO);

    UserDTO findByUsername(String username);

    User findByEmail(String email);

    List<UserDTO> findUsers(String firstName, String lastName);

    UserDTO findById(Long id);

    UserDTO getUserInfo(Authentication authentication);

    List<UserDTO> findUserByCriteria(String firstName, String lastName);
}
