package de.ait.chat.service;

import de.ait.chat.entity.Chat;

import java.util.Optional;

public interface ChatService {

    Chat getChatById(Long id); // Pārliecinieties, ka datu tips ir pareizs

    Chat createChat(Chat chat);

    Chat createChatIfNotExists(String chatName);
}
