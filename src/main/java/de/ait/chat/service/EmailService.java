package de.ait.chat.service;


import de.ait.chat.entity.User;

public interface EmailService {

    void sendConfirmationEmail(User user);

}
