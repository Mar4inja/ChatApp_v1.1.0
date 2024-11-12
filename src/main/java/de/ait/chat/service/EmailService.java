package de.ait.chat.service;


import de.ait.chat.entity.User;
import de.ait.chat.entity.dto.UserDTO;

public interface EmailService {

    void sendConfirmationEmail(User user);

}
