package de.ait.chat.repository;

import de.ait.chat.entity.User;
import de.ait.chat.entity.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<UserDTO> findByFirstNameAndLastName(String firstName, String lastName);
    List<UserDTO> findByFirstName(String firstName);
    List<UserDTO> findByLastName(String lastName);


}
