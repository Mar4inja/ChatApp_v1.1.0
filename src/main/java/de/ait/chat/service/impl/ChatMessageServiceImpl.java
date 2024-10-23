package de.ait.chat.service.impl;

import de.ait.chat.entity.ChatMessage;
import de.ait.chat.repository.ChatMessageRepository;
import de.ait.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository; // Make sure you have this repository

    @Override
    public void saveMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage); // This should save the chat message to the database
    }
}
