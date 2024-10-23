package de.ait.chat.service;


import de.ait.chat.entity.User;

public interface ConfirmationService {

    String generateConfirmationCode(User user);

    boolean activateUser(String code);

}
