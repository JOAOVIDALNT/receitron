package com.joaovidal.receitron.domain.service;

import com.joaovidal.receitron.domain.model.User;
import com.joaovidal.receitron.domain.port.out.TokenProviderPort;
import com.joaovidal.receitron.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private TokenProviderPort tokenProviderPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    String email = "tester@email.com";
    String password = "abc123";
    String hashedPassword = "$adknjn(%sadnaln#@!";
    String token = "ey18y1bnjsapjd91n32k3n1";

    @Test
    void ShouldAuthenticate() {
        User user = new User(UUID.randomUUID(),email, hashedPassword, Set.of("USER"));

        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, hashedPassword)).thenReturn(true);
        when(tokenProviderPort.generateToken(user)).thenReturn(token);

        var result = authService.authenticate(email, password);

        assertEquals(token, result.token());
        verify(userRepositoryPort, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, hashedPassword);
        verify(tokenProviderPort, times(1)).generateToken(user);
    }

    @Test
    void ShouldRegisterUser() {
        User user = new User(UUID.randomUUID(),email, password, Set.of("USER"));

        when(passwordEncoder.encode(password)).thenReturn(hashedPassword);

        authService.register(user);

        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepositoryPort, times(1)).save(user);
    }

    @Test
    void ShouldThrowInvalidCredentials() {
        User user = new User(UUID.randomUUID(),email, hashedPassword, Set.of("USER"));

        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {authService.authenticate(email, password);});
        verify(userRepositoryPort, times(1)).findByEmail(email);
        verify(passwordEncoder, times(0)).matches(password, hashedPassword);
        verify(tokenProviderPort, times(0)).generateToken(user);
    }

}
