package com.demo.molly.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * JWT 工具类：支持 Access Token 与 Refresh Token
 */
@Component
public class JwtUtil {

    @Value("${jwt.access-token-secret}")
    private String accessTokenSecret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-secret}")
    private String refreshTokenSecret;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private SecretKey accessKey;
    private SecretKey refreshKey;

    @PostConstruct
    public void init() {
        this.accessKey = Keys.hmacShaKeyFor(accessTokenSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshKey = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpiration);
        LoginUser loginUser = (LoginUser) userDetails;
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("userId", loginUser.getUser().getId())
                .claim("roles", loginUser.getRoles())
                .claim("permissions", loginUser.getPermissions())
                .claim("type", "access")
                .claim("jti", UUID.randomUUID().toString())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(accessKey, Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenExpiration);
        LoginUser loginUser = (LoginUser) userDetails;
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("userId", loginUser.getUser().getId())
                .claim("type", "refresh")
                .claim("jti", UUID.randomUUID().toString())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(refreshKey, Jwts.SIG.HS256)
                .compact();
    }

    public Claims parseAccessToken(String token) {
        return Jwts.parser()
                .verifyWith(accessKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims parseRefreshToken(String token) {
        return Jwts.parser()
                .verifyWith(refreshKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsernameFromAccessToken(String token) {
        return parseAccessToken(token).getSubject();
    }

    public String getUsernameFromRefreshToken(String token) {
        return parseRefreshToken(token).getSubject();
    }

    public String getJtiFromAccessToken(String token) {
        return parseAccessToken(token).get("jti", String.class);
    }

    public String getJtiFromRefreshToken(String token) {
        return parseRefreshToken(token).get("jti", String.class);
    }

    public Long getUserIdFromAccessToken(String token) {
        return parseAccessToken(token).get("userId", Long.class);
    }

    public Long getUserIdFromRefreshToken(String token) {
        return parseRefreshToken(token).get("userId", Long.class);
    }

    public Date getExpirationFromAccessToken(String token) {
        return parseAccessToken(token).getExpiration();
    }

    public boolean validateAccessToken(String token, UserDetails userDetails) {
        try {
            Claims claims = parseAccessToken(token);
            return !claims.getExpiration().before(new Date())
                    && "access".equals(claims.get("type"))
                    && claims.getSubject().equals(userDetails.getUsername());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token, UserDetails userDetails) {
        try {
            Claims claims = parseRefreshToken(token);
            return !claims.getExpiration().before(new Date())
                    && "refresh".equals(claims.get("type"))
                    && claims.getSubject().equals(userDetails.getUsername());
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> getPermissionsFromAccessToken(String token) {
        return parseAccessToken(token).get("permissions", List.class);
    }
}
