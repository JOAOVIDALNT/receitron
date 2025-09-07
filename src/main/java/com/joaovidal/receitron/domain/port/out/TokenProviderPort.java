package com.joaovidal.receitron.domain.port.out;


import com.joaovidal.receitron.domain.model.User;

import java.util.List;

public interface TokenProviderPort {
    String generateToken(User user);
    boolean isTokenValid(String token, User user);
    String extractUserName(String token);
    List<String> extractRoles(String token);
}
