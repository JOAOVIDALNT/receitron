package com.joaovidal.receitron.domain.port.in;

import com.joaovidal.receitron.adapter.in.web.dto.UserLoginResponse;

public interface LoginUseCase {
    UserLoginResponse authenticate(String userName, String password);
}
