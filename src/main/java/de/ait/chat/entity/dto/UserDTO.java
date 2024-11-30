package de.ait.chat.entity.dto;

import de.ait.chat.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String email;
    private boolean isActive;
    private String password;
    private Set<Role> roles;
    private LocalDateTime registrationDate;

    // Конструктор для первого имени
    public UserDTO(String firstName) {
        this.firstName = firstName;
    }

    // Конструктор для первого и последнего имени
    public UserDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
