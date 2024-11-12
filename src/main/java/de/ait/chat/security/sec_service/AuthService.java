package de.ait.chat.security.sec_service;

import de.ait.chat.entity.User;
import de.ait.chat.exceptions.AuthException;
import de.ait.chat.security.sec_dto.TokenResponseDto;
import de.ait.chat.service.UserService;
import io.jsonwebtoken.Claims;
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


    public AuthService(BCryptPasswordEncoder encoder, TokenService tokenService, UserService userService) {
        this.encoder = encoder;
        this.tokenService = tokenService;
        this.userService = userService;
        this.refreshStorage = new HashMap<>();

    }

    public TokenResponseDto login(@NonNull User inboundUser) throws AuthException {
        String username = inboundUser.getEmail();
        User foundUser = userService.findByEmail(username);

        if (foundUser == null) {
            throw new AuthException("User not found");
        }

        if (!isRegistrationConfirmed(foundUser)) {
            throw new AuthException("E-mail confirmation was not completed");
        }

        if (encoder.matches(inboundUser.getPassword(), foundUser.getPassword())) {
            String accessToken = tokenService.generateAccessToken(foundUser);
            String refreshToken = tokenService.generateRefreshToken(foundUser);
            refreshStorage.put(username, refreshToken);
            return new TokenResponseDto(accessToken, refreshToken);
        } else {
            throw new AuthException("Password is incorrect");
        }
    }

    public TokenResponseDto getAccessToken(@NonNull String inboundRefreshToken) {
        Claims refreshClaims = tokenService.getRefreshClaims(inboundRefreshToken);
        String username = refreshClaims.getSubject();
        String savedRefreshToken = refreshStorage.get(username);

        if (inboundRefreshToken.equals(savedRefreshToken)) {
            User user = userService.findByEmail(username);
            String accessToken = tokenService.generateAccessToken(user);
            return new TokenResponseDto(accessToken, null);
        }
        return new TokenResponseDto(null, null);
    }


    private boolean isRegistrationConfirmed(User user) {
        return user.isActive();
    }


}
