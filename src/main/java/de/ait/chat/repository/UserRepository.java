package de.ait.chat.repository;

import de.ait.chat.entity.User;
import de.ait.chat.entity.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("SELECT new de.ait.chat.entity.dto.UserDTO(u.firstName, u.lastName) FROM User u WHERE LOWER(TRIM(u.firstName)) = LOWER(TRIM(:firstName)) AND LOWER(TRIM(u.lastName)) = LOWER(TRIM(:lastName))")
    List<UserDTO> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT new de.ait.chat.entity.dto.UserDTO(u.firstName, u.lastName) FROM User u WHERE LOWER(TRIM(u.firstName)) = LOWER(TRIM(:firstName))")
    List<UserDTO> findByFirstName(@Param("firstName") String firstName);

    @Query("SELECT new de.ait.chat.entity.dto.UserDTO(u.firstName, u.lastName) FROM User u WHERE LOWER(TRIM(u.lastName)) = LOWER(TRIM(:lastName))")
    List<UserDTO> findByLastName(@Param("lastName") String lastName);

}
