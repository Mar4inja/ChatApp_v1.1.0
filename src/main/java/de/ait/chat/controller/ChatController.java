package de.ait.chat.controller;

import de.ait.chat.entity.ChatMessage;
import de.ait.chat.service.ChatMessageService;
import de.ait.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final UserService userService;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessage sendMessage(@DestinationVariable Long roomId, @Payload ChatMessage chatMessage) {
        // Saglabā ziņu datu bāzē
        chatMessage.setRoomId(roomId); // Iestatām roomId ziņai
        chatMessageService.saveMessage(chatMessage); // Saglabājam ziņu datu bāzē

        return chatMessage; // Atgriežam ziņu
    }

    @MessageMapping("/chat.addUser/{roomId}")
    public ChatMessage addUser(@DestinationVariable String roomId, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        // Opcionalitātes līmenī var reģistrēt vai apstrādāt lietotāju pievienošanos
        return chatMessage;
    }
}
//TODO messages don't save in db!