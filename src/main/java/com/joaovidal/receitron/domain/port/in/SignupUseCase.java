package com.joaovidal.receitron.domain.port.in;

import com.joaovidal.receitron.domain.model.User;

public interface SignupUseCase {
    void register(User user);
}
