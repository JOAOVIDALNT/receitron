package com.joaovidal.receitron.domain.port.in;

import com.joaovidal.receitron.domain.model.User;

import java.util.List;

public interface UpdateUserUseCase {
    User addCultures(String email, List<String> cultures);
    User addPreferences(String email, List<String> preferences);
    User addRestrictions(String email, List<String> restrictions);
}
