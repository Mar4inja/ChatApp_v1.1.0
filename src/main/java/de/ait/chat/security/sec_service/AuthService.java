package de.ait.chat.security.sec_service;
import de.ait.chat.entity.User;
import de.ait.chat.security.sec_dto.TokenResponseDto;
import de.ait.chat.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;
    private final Map<String, String> refreshStorage;
    private final BCryptPasswordEncoder encoder;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(BCryptPasswordEncoder encoder, TokenService tokenService, UserService userService) {
        this.encoder = encoder;
        this.tokenService = tokenService;
        this.userService = userService;
        this.refreshStorage = new HashMap<>();

    }

    public TokenResponseDto login(@NonNull User inboundUser) throws AuthException {
        try {
            String username = inboundUser.getEmail();
            logger.info("Attempting to find user with email: " + username);
            User foundUser = userService.findByEmail(username);

            if (foundUser == null) {
                logger.warn("User not found with email: " + username);
                throw new AuthException("User not found");  // Šeit mēs varam uztvert šo kļūdu klienta pusē kā **401**
            }

//            if (!isRegistrationConfirmed(foundUser)) {
//                logger.warn("E-mail confirmation not completed for user: " + username);
//                throw new AuthException("E-mail confirmation was not completed");  // Šeit mēs varam uztvert šo kļūdu klienta pusē kā **401**
//            }

            if (encoder.matches(inboundUser.getPassword(), foundUser.getPassword())) {
                logger.info("Password matches for user: " + username);
                String accessToken = tokenService.generateAccessToken(foundUser);
                String refreshToken = tokenService.generateRefreshToken(foundUser);
                refreshStorage.put(username, refreshToken);
                logger.info("Tokens generated for user: " + username);
                return new TokenResponseDto(accessToken, refreshToken, foundUser);
            } else {
                logger.warn("Incorrect password for user: " + username);
                throw new AuthException("Password is incorrect");  // Šeit mēs varam uztvert šo kļūdu klienta pusē kā **401**
            }
        } catch (AuthException e) {
            throw e;  // **401** kļūda tiks iestūrta uz `login` metodi kontrolierī
        } catch (Exception e) {
            logger.error("Unexpected error during login: {}", e.getMessage());
            throw new AuthException("Unexpected error during login");  // **500** kļūda, ja notiek neparedzēta kļūda
        }
    }


    public TokenResponseDto getAccessToken(@NonNull String inboundRefreshToken) {
        Claims refreshClaims = tokenService.getRefreshClaims(inboundRefreshToken);
        String username = refreshClaims.getSubject();
        String savedRefreshToken = refreshStorage.get(username);

        if (savedRefreshToken == null) {
            logger.warn("No refresh token found for user: " + username);
            return new TokenResponseDto(null, null);
        }

        if (inboundRefreshToken.equals(savedRefreshToken)) {
            User user = userService.findByEmail(username);

            if (user == null) {
                logger.warn("User not found for email: " + username);
                return new TokenResponseDto(null, null);
            }

            String accessToken = tokenService.generateAccessToken(user);
            return new TokenResponseDto(accessToken, null);
        } else {
            logger.warn("Refresh token mismatch for user: " + username);
        }

        return new TokenResponseDto(null, null);
    }
}
