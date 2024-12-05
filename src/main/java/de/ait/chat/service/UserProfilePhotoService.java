package de.ait.chat.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserProfilePhotoService {

    String uploadProfilePhoto(Long userId, MultipartFile file) throws IOException;
    byte[] getProfilePhoto(Long userId) throws IOException;
}
