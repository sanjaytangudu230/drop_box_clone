package com.drop_box_clone.config;

import com.drop_box_clone.dto.pojos.AuthenticatedInfo;
import com.drop_box_clone.exception.DropBoxException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.WebApplicationContext;
import io.jsonwebtoken.Claims;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;

@Configuration
public class AuthenticatedConfig {

    @Value("${user.secret.key}")
    private String secretKey;

    @Bean
    @Primary
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public AuthenticatedInfo requestScopedBean(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("Authorization");

            Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey),
                    SignatureAlgorithm.HS256.getJcaName());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(jwtToken).getBody();
            return new AuthenticatedInfo(claims.getSubject());
        } catch (Exception ex) {
            throw new DropBoxException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }
}
