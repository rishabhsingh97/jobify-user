package com.rsuniverse.jobify_user.utils;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import com.rsuniverse.jobify_user.models.enums.UserRole;
import com.rsuniverse.jobify_user.models.enums.UserStatus;
import com.rsuniverse.jobify_user.models.pojos.AuthUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JwtUtils {

    private final static String SECRET_KEY = "your-very-secure-base64url-encoded-secret-key";

    private static SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String generateToken(AuthUser user) {
        Map<String, Object> claims = makePayload(user);
        return createToken(claims, "jobify:user_access_token", 1000 * 60 * 60);
    }

    public static String generateRefreshToken(AuthUser user) {
        Map<String, Object> claims = makePayload(user);
        return createToken(claims, "jobify:user_refresh_token", 1000 * 60 * 60 * 24);
    }

    private static Map<String,Object> makePayload(AuthUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("fullname", user.getFullName());
        claims.put("email", user.getEmail());
        claims.put("status", user.getStatus());
        claims.put("roles", user.getRoles());
        return claims;
    }

    private static String createToken(Map<String, Object> claims, String subject, long expiration) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expiration)))
                .and()
                .signWith(getKey())
                .compact();
    }

    private static <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public static Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static boolean validateToken(String token, String username) {
        final String extractedUsername = extractSubject(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(Date.from(Instant.now()));
    }

    public static String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @SuppressWarnings("unchecked")
    public static Set<UserRole> extractRoles(String token) {
        List<String> rolesList = extractClaim(token, claims -> (List<String>) claims.get("roles"));

        return rolesList.stream()
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());
    }

    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static AuthUser extractUser(String token) {
        String id = extractClaim(token, claims -> (String) claims.get("id"));
        String name = extractClaim(token, claims -> (String) claims.get("name"));
        String email = extractClaim(token, claims -> (String) claims.get("email"));
        String status = extractClaim(token, claims -> (String) claims.get("status"));
        Set<UserRole> roles = extractRoles(token);

        AuthUser user = new AuthUser();

        String[] nameParts = name.split(" ");
        user.setId(id);
        user.setEmail(email);
        user.setStatus(UserStatus.valueOf(status));
        user.setRoles(roles);

        return user;
    }
}
