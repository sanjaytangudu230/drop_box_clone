package com.drop_box_clone.service;

import com.drop_box_clone.dto.requests.UserRequest;
import com.drop_box_clone.entites.User;
import com.drop_box_clone.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Value("${user.secret.key}")
    private String secretKey;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static String hashString(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hashStringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hashStringBuilder.append(String.format("%02x", b));
            }

            return hashStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not available", e);
        }
    }

    @Override
    public String createUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(hashString(userRequest.getPassword()));
        userRepository.save(user);
        return getJwtToken(secretKey, user.getId().toString());
    }

    @Override
    public String login(String userName, String password) {
        User user = userRepository.findByUsername(userName);
        String hashedPassword = hashString(password);
        if (!hashedPassword.equals(user.getPassword())) {
            return "Invalid password";
        }
        return getJwtToken(secretKey, user.getId().toString());
    }

    private String getJwtToken(String secretKey, String userId) {
        try {
            Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey),
                    SignatureAlgorithm.HS256.getJcaName());
            Instant now = Instant.now();
            return Jwts.builder()
                    .setSubject(userId)
                    .setIssuedAt(Date.from(now))
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                    .signWith(hmacKey)
                    .compact();
        } catch (Exception e) {
            log.info("Error while creating jwt token with secret {} - {}", secretKey, e.toString());
            return null;
        }
    }

    public User FetchUserById(UUID userId) {
        Optional<User> users = userRepository.findById(userId);
        return users.orElse(null);
    }

    public User FetchUserByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

}
