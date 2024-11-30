package de.ait.chat.repository;

import de.ait.chat.entity.User;
import de.ait.chat.entity.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    // Ja vēlaties izmantot DTO tieši vaicājumos
    @Query("SELECT new de.ait.chat.entity.dto.UserDTO(u.firstName, u.lastName) FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName")
    List<UserDTO> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT new de.ait.chat.entity.dto.UserDTO(u.firstName, u.lastName) FROM User u WHERE u.firstName = :firstName")
    List<UserDTO> findByFirstName(@Param("firstName") String firstName);

    @Query("SELECT new de.ait.chat.entity.dto.UserDTO(u.firstName, u.lastName) FROM User u WHERE u.lastName = :lastName")
    List<UserDTO> findByLastName(@Param("lastName") String lastName);
}
