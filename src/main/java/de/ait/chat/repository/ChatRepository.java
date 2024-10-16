package de.ait.chat.repository;

import de.ait.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByName(String name);

    List<Chat> findByCreatedAtAfter(LocalDateTime date);

}
