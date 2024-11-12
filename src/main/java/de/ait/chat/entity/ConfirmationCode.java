package de.ait.chat.entity;

import de.ait.chat.entity.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "confirm_code")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ConfirmationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "expired")
    private LocalDateTime expired;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    public ConfirmationCode(String code, LocalDateTime expired, User user) {
        this.code = code;
        this.expired = expired;
        this.user = user;
    }
}