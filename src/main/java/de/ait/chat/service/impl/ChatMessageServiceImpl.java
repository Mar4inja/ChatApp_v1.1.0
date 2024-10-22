package de.ait.chat.service.impl;

import de.ait.chat.entity.ChatMessage;
import de.ait.chat.repository.ChatMessageRepository;
import de.ait.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository; // Make sure you have this repository

    @Transactional
    public void saveMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage); // This should save the chat message to the database
    }
}
