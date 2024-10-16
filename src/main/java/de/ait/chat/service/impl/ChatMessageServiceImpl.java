package de.ait.chat.service.impl;

import de.ait.chat.entity.ChatMessage;
import de.ait.chat.repository.ChatMessageRepository;
import de.ait.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public void saveMessage(ChatMessage message) {
        chatMessageRepository.save(message);
    }
}
