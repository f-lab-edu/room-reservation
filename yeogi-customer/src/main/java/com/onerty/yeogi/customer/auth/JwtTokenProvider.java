package com.onerty.yeogi.customer.auth;

import com.onerty.yeogi.customer.auth.dto.JwtPayload;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String SECRET_KEY = "TEMP_SECRET_KEY";
    private final long ACCESS_TOKEN_EXPIRATION = 60 * 60 * 1000;  // 1시간
    private final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7일

    public String generateAccessToken(JwtPayload jwtPayload) {
        return Jwts.builder()
                .setSubject(jwtPayload.userIdentifier())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(JwtPayload jwtPayload) {
        return Jwts.builder()
                .setSubject(jwtPayload.userIdentifier())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public long getExpiration(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody().getExpiration().getTime() - System.currentTimeMillis();
    }
}
