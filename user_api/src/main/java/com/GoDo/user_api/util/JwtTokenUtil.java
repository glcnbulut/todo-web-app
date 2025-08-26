// JWT token üretimi, doğrulama ve çözümleme işlemlerini yapan yardımcı sınıf.
// JWT token üretimi, doğrulama ve çözümleme işlemlerini yapan yardımcı sınıf.
package com.GoDo.user_api.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret:A_SECRET_KEY_WITH_AT_LEAST_256_BITS_FOR_HS256_ALGORITHM}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24 hours
    private Long expiration;

    // Kullanıcı bilgisiyle JWT token üretir
    // Kullanıcı bilgisiyle JWT token üretir
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
        return createToken(claims, userDetails.getUsername());
    }

    // Token oluşturma işlemi
    // Token oluşturma işlemi
    private String createToken(Map<String, Object> claims, String subject) {
    Key key = getSigningKey();
    return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Token geçerli mi kontrol eder
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Token'dan kullanıcı adını (email) çıkarır
    // Token'dan kullanıcı adını (email) çıkarır
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Token süresi dolmuş mu kontrol eder
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Token'ın son geçerlilik tarihini döndürür
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Token'dan claim çıkarır
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Token'dan tüm claim'leri çıkarır
    private Claims extractAllClaims(String token) {
        Key key = getSigningKey();
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // Attempts to decode the configured secret as Base64; if that fails,
    // derive a 256-bit key from the raw secret using SHA-256 so short/plain
    // secrets work in tests and local runs.
    private Key getSigningKey() {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (Exception e) {
            try {
                MessageDigest sha = MessageDigest.getInstance("SHA-256");
                keyBytes = sha.digest(secret.getBytes(StandardCharsets.UTF_8));
            } catch (NoSuchAlgorithmException ex) {
                // Should never happen for SHA-256; rethrow as runtime if it does
                throw new IllegalStateException(ex);
            }
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
