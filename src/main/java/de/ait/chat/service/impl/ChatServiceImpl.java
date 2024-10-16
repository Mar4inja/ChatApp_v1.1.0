package de.ait.chat.service.impl;

import de.ait.chat.entity.Chat;
import de.ait.chat.repository.ChatRepository;
import de.ait.chat.service.ChatService;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public Chat getChatById(Long id) {
        return chatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found with id: " + id));
    }

    @Override
    public Chat createChat(Chat chat) {
        return chatRepository.save(chat); // Izveido jaunu sarakstu
    }

    @Override
    public Chat createChatIfNotExists(String chatName) {
        Optional<Chat> existingChat = chatRepository.findByName(chatName); // Pārbaudām, vai čats jau pastāv
        if (existingChat.isPresent()) {
            return existingChat.get(); // Atgriežam esošo čatu
        }

        Chat newChat = new Chat();
        newChat.setName(chatName); // Iestatām čata nosaukumu
        return chatRepository.save(newChat); // Saglabājam jauno čatu
    }
}
