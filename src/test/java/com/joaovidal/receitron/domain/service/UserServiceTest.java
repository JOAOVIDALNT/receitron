package com.joaovidal.receitron.domain.service;

import com.joaovidal.receitron.adapter.in.exception.ApiException;
import com.joaovidal.receitron.domain.model.User;
import com.joaovidal.receitron.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepositoryPort repositoryPort;

    @InjectMocks
    private UserService userService;

    String email = "tester@email.com";

    @Test
    void shouldFindUserByEmail() {
        User user = new User(UUID.randomUUID(),email, "password", Set.of("USER"));
        when(repositoryPort.findByEmail(email)).thenReturn(Optional.of(user));

        var result = userService.findByEmail(email).get();

        assertEquals(result.getEmail(), email);
        verify(repositoryPort, times(1)).findByEmail(email);
    }

    @Test
    void shouldThrowUserNotFound() {
        User user = new User(UUID.randomUUID(),email, "password", Set.of("USER"));
        when(repositoryPort.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> {userService.findByEmail(email);});
        verify(repositoryPort, times(1)).findByEmail(email);
    }
}
