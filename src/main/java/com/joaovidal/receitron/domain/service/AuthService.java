package com.joaovidal.receitron.domain.service;

import com.joaovidal.receitron.adapter.in.exception.ApiException;
import com.joaovidal.receitron.adapter.in.web.dto.UserLoginResponse;
import com.joaovidal.receitron.domain.model.User;
import com.joaovidal.receitron.domain.port.in.LoginUseCase;
import com.joaovidal.receitron.domain.port.in.SignupUseCase;
import com.joaovidal.receitron.domain.port.out.TokenProviderPort;
import com.joaovidal.receitron.domain.port.out.UserRepositoryPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements SignupUseCase, LoginUseCase {

    private final UserRepositoryPort userRepository;
    private final TokenProviderPort tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepositoryPort userRepository, TokenProviderPort tokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public UserLoginResponse authenticate(String email, String password) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND)); // TODO: CUSTOM EX

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ApiException("Invalid login", HttpStatus.UNAUTHORIZED);
        }

        return new UserLoginResponse(tokenProvider.generateToken(user), user.getRoles());
    }

}
