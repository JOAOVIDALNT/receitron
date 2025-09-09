package com.joaovidal.receitron.adapter.in.web.dto;

import java.util.Set;

public record UpdateUserResponse(String email, Set<String> preferences, Set<String> favoriteCultures, Set<String> restrictions) {
}
