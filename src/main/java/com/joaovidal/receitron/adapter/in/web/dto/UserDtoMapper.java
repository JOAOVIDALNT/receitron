package com.joaovidal.receitron.adapter.in.web.dto;

import com.joaovidal.receitron.domain.model.User;

import java.util.Set;
import java.util.UUID;

public class UserDtoMapper {

    public static User toDomain(UserSignupRequest request) {
        return new User(UUID.randomUUID(), request.email(), request.password(), Set.of("USER"));
    }
}
