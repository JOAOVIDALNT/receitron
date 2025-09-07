package com.joaovidal.receitron.adapter.in.web.dto;

import java.util.Set;

public record UserLoginResponse(String token, Set<String> roles) {
}
