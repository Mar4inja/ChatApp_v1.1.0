package de.ait.chat.controller;

import de.ait.chat.service.impl.UserProfilePhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfilePhotoController {

    private final UserProfilePhotoService photoService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file, Authentication authentication) {
        try {
            // Iegūst lietotāja informāciju no autentifikācijas
            String userEmail = authentication.getName(); // Vai arī izmantojiet `authentication.getPrincipal()`

            // Augšupielādējiet attēlu
            String message = photoService.uploadProfilePhoto(userEmail, file);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload photo: " + e.getMessage());
        }
    }
}
