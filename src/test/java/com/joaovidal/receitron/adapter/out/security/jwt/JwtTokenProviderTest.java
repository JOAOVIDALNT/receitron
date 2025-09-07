package com.joaovidal.receitron.adapter.out.security.jwt;

import com.joaovidal.receitron.domain.model.User;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider provider;

    @Test
    void shouldGenerateAndValidateToken() {
        var user = new User(UUID.randomUUID(), "tester", "passsss", Set.of("USER"));
        var token = provider.generateToken(user);

        assertTrue(token.startsWith("ey"));
        assertTrue(provider.isTokenValid(token, user));
        assertEquals(provider.extractUserName(token), user.getEmail());
    }

    @Test
    void shouldThrowDueToInvalidSignature() {
        var user = new User(UUID.randomUUID(), "tester", "passsss", Set.of("USER"));
        var token = provider.generateToken(user);

        assertThrows(SignatureException.class, () -> { provider.isTokenValid(token + "birotoken", user);});
    }
}
