package de.ait.chat.security.sec_service;

import de.ait.chat.entity.Role;
import de.ait.chat.entity.User;
import de.ait.chat.repository.RoleRepository;
import de.ait.chat.security.AuthInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class TokenService {

    private SecretKey accessKey;
    private SecretKey refreshKey;
    private RoleRepository roleRepository;

    public TokenService(@Value("${key.access}") String accessKey, @Value("${key.refresh}") String refreshKey, RoleRepository roleRepository) {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
        this.roleRepository = roleRepository;
    }

    public String generateAccessToken(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Проверяем, что необходимые поля пользователя не равны null
        if (user.getEmail() == null || user.getFirstName() == null || user.getLastName() == null || user.getBirthdate() == null) {
            throw new IllegalArgumentException("User email, first name, and last name must not be null");
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        Instant expirationInstant = currentDateTime.plusDays(7).atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(expirationInstant);

        return Jwts.builder()
                .subject(user.getEmail())
                .expiration(expirationDate)
                .signWith(accessKey)
                .claim("roles", user.getAuthorities())
                .claim("name", user.getFirstName() + " " + user.getLastName())
                .claim("birthDate", user.getBirthdate().toString())
                .compact();
    }

    public String generateRefreshToken(User user) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Instant expirationInstant = currentDateTime.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(expirationInstant);

        return Jwts.builder().subject(user.getEmail()).expiration(expirationDate).signWith(refreshKey).compact();
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, accessKey);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, refreshKey);
    }

    public boolean validateToken(String token, SecretKey key) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getAccessClaims(String accessToken) {
        return getClaims(accessToken, accessKey);
    }

    public Claims getRefreshClaims(String refreshToken) {
        return getClaims(refreshToken, refreshKey);
    }


    private Claims getClaims(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public AuthInfo generateAuthInfo(Claims claims) {
        String username = claims.getSubject();

        // Getting roles from the claims
        List<LinkedHashMap<String, String>> rolesList = (List<LinkedHashMap<String, String>>) claims.get("roles");
        Set<Role> roles = new HashSet<>();

        for (LinkedHashMap<String, String> roleEntry : rolesList) {
            String roleName = roleEntry.get("authority");
            Role role = roleRepository.findByTitle(roleName);
            roles.add(role);
        }

        // Extracting birthDate as String from the claims and then parsing it to LocalDate
        String birthDateString = (String) claims.get("birthDate");
        LocalDate birthDate = LocalDate.parse(birthDateString);  // Convert the String to LocalDate

        // Creating and returning the AuthInfo object
        return new AuthInfo(roles, username, birthDate);
    }

}