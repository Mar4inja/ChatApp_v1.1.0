package de.ait.chat.service;


import de.ait.chat.entity.ChatMessage;

public interface ChatMessageService {

    void saveMessage(ChatMessage chatMessage);
}
