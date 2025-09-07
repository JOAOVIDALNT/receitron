package com.joaovidal.receitron.domain.service;

import com.joaovidal.receitron.domain.model.User;
import com.joaovidal.receitron.domain.port.in.FindUserUseCase;
import com.joaovidal.receitron.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements FindUserUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public UserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        var user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return Optional.of(user);
    }
}
