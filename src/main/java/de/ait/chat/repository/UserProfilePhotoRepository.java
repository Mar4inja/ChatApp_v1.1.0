package de.ait.chat.repository;

import de.ait.chat.entity.User;
import de.ait.chat.entity.UserProfilePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfilePhotoRepository  extends JpaRepository<UserProfilePhoto, Long> {

    UserProfilePhoto findByUser_Id(Long userId);
    UserProfilePhoto findByUser(User user);
}
