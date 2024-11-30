package de.ait.chat.service.impl;

import de.ait.chat.entity.User;
import de.ait.chat.entity.dto.UserDTO;
import de.ait.chat.exceptions.UserNotFoundException;
import de.ait.chat.repository.RoleRepository;
import de.ait.chat.repository.UserRepository;
import de.ait.chat.service.EmailService;
import de.ait.chat.service.UserService;
import de.ait.chat.service.mapping.UserMappingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;
    private final EmailService emailService;
    private final UserMappingService userMappingService;


    @Override
    public UserDTO register(UserDTO userDTO) {
        userDTO.setId(null);
        if (userDTO.getFirstName() == null || userDTO.getFirstName().isEmpty()
                || userDTO.getLastName() == null || userDTO.getLastName().isEmpty()
                || userDTO.getBirthdate() == null
                || userDTO.getEmail() == null || userDTO.getEmail().isEmpty()
                || userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("All fields are required");
        }
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new IllegalArgumentException("Email address already in use");
        }
        userDTO.setRoles(Collections.singleton(roleRepository.findByTitle("ROLE_USER")));
        validatePassword(userDTO.getPassword());
        userDTO.setRegistrationDate(LocalDateTime.now());
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        userDTO.setActive(false);
        User user = userMappingService.mapDtoToEntity(userDTO);
        User savedUser = userRepository.save(user);
        emailService.sendConfirmationEmail(user);
        logger.info("User with " + userDTO.getEmail() + " successfully registered");
        return userMappingService.mapEntityToDto(savedUser);
    }

    @Override
    public UserDTO updateData(Authentication authentication, UserDTO updatedUserDTO) {
        // Iegūstam pašreizējo lietotāju pēc e-pasta
        User currentUser = findByEmail(authentication.getName());
        if (currentUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        boolean isUpdated = false;

        // Pārbauda un atjaunina vārdu
        if (updatedUserDTO.getFirstName() != null && !updatedUserDTO.getFirstName().isEmpty()) {
            currentUser.setFirstName(updatedUserDTO.getFirstName());
            isUpdated = true;
        }

        // Pārbauda un atjaunina uzvārdu
        if (updatedUserDTO.getLastName() != null && !updatedUserDTO.getLastName().isEmpty()) {
            currentUser.setLastName(updatedUserDTO.getLastName());
            isUpdated = true;
        }

        // Pārbauda un atjaunina dzimšanas datumu
        if (updatedUserDTO.getBirthdate() != null) {
            currentUser.setBirthdate(updatedUserDTO.getBirthdate());
            isUpdated = true;
        }

        // Ja bija atjauninājumi, saglabā tos
        if (isUpdated) {
            return userMappingService.mapEntityToDto(userRepository.save(currentUser));
        } else {
            throw new IllegalArgumentException("No valid fields to update");
        }
    }


    @Override
    public UserDTO getUserInfo(Authentication authentication) {
        String username = authentication.getName();
        User currentUser = findByEmail(username);  // Šeit atgriež User, nevis UserDTO

        if (currentUser == null) {
            throw new NoSuchElementException("User not found");
        }

        return userMappingService.mapEntityToDto(currentUser);  // Pārvērst User uz UserDTO
    }

    @Override
    public List<UserDTO> findByFirstName(String firstName) {
        // Pārbauda, vai firstName ir null
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("Vārds nedrīkst būt null vai tukšs");
        }
        // Meklē lietotājus pēc pirmā vārda
        return userRepository.findByFirstName(firstName); // Jau atgriež List<UserDTO>
    }

    @Override
    public List<UserDTO> findByLastName(String lastName) {
        // Pārbauda, vai lastName ir null
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Uzvārds nedrīkst būt null vai tukšs");
        }
        // Meklē lietotājus pēc uzvārda
        return userRepository.findByLastName(lastName); // Jau atgriež List<UserDTO>
    }

    public List<UserDTO> findByNameAndLastName(String firstName, String lastName) {
        List<UserDTO> users;
        // Ja abi parametri ir norādīti
        if (firstName != null && lastName != null) {
            users = userRepository.findByFirstNameAndLastName(firstName, lastName);
        }
        // Ja tikai firstName ir norādīts
        else if (firstName != null) {
            users = userRepository.findByFirstName(firstName);
        }
        // Ja tikai lastName ir norādīts
        else if (lastName != null) {
            users = userRepository.findByLastName(lastName);
        }
        // Ja abi parametri ir null
        else {
            throw new IllegalArgumentException("Both firstName and lastName cannot be null");
        }
        // Ja nav atrasti lietotāji
        if (users == null || users.isEmpty()) {
            throw new UserNotFoundException("User not found with given criteria");
        }
        return users;
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
    public UserDTO findByUsername(String username) {
        User user = userRepository.findByEmail(username); // Assuming email is used as the username
        if (user == null) {
            throw new UserNotFoundException("User not found with email: " + username);
        }
        return userMappingService.mapEntityToDto(user); // Pārvērst User uz UserDTO
    }



    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserDTO> findUsers(String firstName, String lastName) {
        List<UserDTO> users;
        if (firstName != null && lastName != null) {
            users = userRepository.findByFirstNameAndLastName(firstName, lastName);
        } else if (firstName != null) {
            users = userRepository.findByFirstName(firstName);
        } else if (lastName != null) {
            users = userRepository.findByLastName(lastName);
        } else {
            throw new IllegalArgumentException("Both firstName and lastName cannot be null");
        }
        if (users == null || users.isEmpty()) {
            throw new UserNotFoundException("User not found with given criteria");
        }
        return users;
    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        // Pārvēršam User entitāti uz UserDTO
        return userMappingService.mapEntityToDto(user);
    }

}