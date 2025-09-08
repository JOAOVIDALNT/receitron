package com.joaovidal.receitron.domain.port.in;

import com.joaovidal.receitron.domain.model.User;

import java.util.Optional;

public interface FindUserUseCase {
    Optional<User> findByEmail(String email);
}
