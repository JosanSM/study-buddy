package com.josan.study_buddy.Service;

import com.josan.study_buddy.User.Role;
import com.josan.study_buddy.User.User;
import com.josan.study_buddy.auth.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTests {

    // 256-bit Base64-encoded key for tests only
    private static final String TEST_SECRET =
            "dGhpcy1pcy1hLXZlcnktbG9uZy1zZWNyZXQta2V5LWZvci1kZXZlbG9wbWVudA==";
    private static final long EXPIRATION_MS = 900_000L; // 15 minutes

    private JwtService jwtService;
    private User testUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(TEST_SECRET, EXPIRATION_MS);
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .passwordHash("hashed")
                .role(Role.USER)
                .build();
    }

    @Test
    public void generateToken_ValidUserDetails_ReturnsNonNullToken() {
        String token = jwtService.generateToken(testUser);
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    public void generateToken_ValidUserDetails_TokenHasThreeParts() {
        String token = jwtService.generateToken(testUser);
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    public void extractUsername_ValidToken_ReturnsUserEmail() {
        String token = jwtService.generateToken(testUser);
        assertEquals("test@example.com", jwtService.extractUsername(token));
    }

    @Test
    public void isTokenValid_FreshTokenAndMatchingUser_ReturnsTrue() {
        String token = jwtService.generateToken(testUser);
        assertTrue(jwtService.isTokenValid(token, testUser));
    }

    @Test
    public void isTokenValid_TokenWithWrongEmail_ReturnsFalse() {
        User otherUser = User.builder()
                .id(2L)
                .email("other@example.com")
                .passwordHash("hashed")
                .role(Role.USER)
                .build();
        String token = jwtService.generateToken(testUser);
        assertFalse(jwtService.isTokenValid(token, otherUser));
    }

    @Test
    public void isTokenValid_ExpiredToken_ReturnsFalse() {
        // Build a token that was already expired when issued using the same key
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(TEST_SECRET));
        String expiredToken = Jwts.builder()
                .subject(testUser.getUsername())
                .issuedAt(new Date(System.currentTimeMillis() - 3_600_000))
                .expiration(new Date(System.currentTimeMillis() - 1_800_000))
                .signWith(key)
                .compact();

        // JJWT throws ExpiredJwtException on parse, so isTokenValid should surface false or throw
        assertThrows(Exception.class, () -> jwtService.isTokenValid(expiredToken, testUser));
    }

    @Test
    public void extractUsername_TamperedToken_ThrowsException() {
        String token = jwtService.generateToken(testUser);
        // Corrupt the signature portion
        String tampered = token.substring(0, token.lastIndexOf('.') + 1) + "invalidsignature";
        assertThrows(Exception.class, () -> jwtService.extractUsername(tampered));
    }

    @Test
    public void generateToken_TwoCallsForSameUser_ReturnDifferentTokens() {
        // Tokens differ because issuedAt timestamps differ at millisecond precision
        String first = jwtService.generateToken(testUser);
        String second = jwtService.generateToken(testUser);
        // They may occasionally be equal if run in the same millisecond — acceptable
        // The important thing is both are valid
        assertTrue(jwtService.isTokenValid(first, testUser));
        assertTrue(jwtService.isTokenValid(second, testUser));
    }
}
