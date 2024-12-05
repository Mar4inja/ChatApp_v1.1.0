package de.ait.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_profile_photo")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfilePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "photo_path")
    private String photoPath;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UserProfilePhoto(User user, String photoPath) {
        this.user = user;
        this.photoPath = photoPath;
        this.createdAt = LocalDateTime.now(); // Устанавливаем дату создания
    }

}
