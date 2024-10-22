package de.ait.chat.entity;

import jakarta.persistence.*;
import de.ait.chat.enums.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automātiska id ģenerācija
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MessageType type;

    @Column(name = "content")
    private String content;

    @Column(name = "sender")
    private String sender;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne // Establishing the relationship with the User entity
    @JoinColumn(name = "user_id") // This will create a foreign key in the chat_message table
    private User user;

    @Column(name = "room_id") // Pievienots room_id kā kolonna
    private Long roomId; // Identificētājs konkrētai istabai

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(); // Iestata current laiku pirms ieraksta izveides
    }
}
