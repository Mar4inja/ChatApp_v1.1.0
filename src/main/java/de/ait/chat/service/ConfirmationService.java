package de.ait.chat.service;


import de.ait.chat.entity.User;
import de.ait.chat.entity.dto.UserDTO;

public interface ConfirmationService {

    String generateConfirmationCode(User user);

    boolean activateUser(String code);

}
