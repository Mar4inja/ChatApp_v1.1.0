package de.ait.chat.service.impl;

import de.ait.chat.entity.User;
import de.ait.chat.entity.UserProfilePhoto;
import de.ait.chat.repository.UserProfilePhotoRepository;
import de.ait.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class UserProfilePhotoService {

    private final UserProfilePhotoRepository photoRepository;
    private final UserRepository userRepository;

    @Value("${profile.photo.upload.dir}")
    private String uploadDir;

    public String uploadProfilePhoto(String userEmail, MultipartFile file) throws IOException {
        // Get user from the database by email
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Create the upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Create a unique file name linked to the user
        String fileName = user.getId() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Save the file
        Files.write(filePath, file.getBytes());

        // Update or add the profile photo
        UserProfilePhoto profilePhoto = photoRepository.findByUser(user);
        if (profilePhoto == null) {
            profilePhoto = new UserProfilePhoto();
            profilePhoto.setUser(user);
        }
        profilePhoto.setPhotoPath(filePath.toString());
        photoRepository.save(profilePhoto);

        return "Profile photo uploaded successfully!";
    }
}
