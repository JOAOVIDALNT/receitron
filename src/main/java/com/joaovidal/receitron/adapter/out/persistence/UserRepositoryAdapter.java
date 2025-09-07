package com.joaovidal.receitron.adapter.out.persistence;

import com.joaovidal.receitron.adapter.out.persistence.mapper.UserEntityMapper;
import com.joaovidal.receitron.adapter.out.persistence.repository.UserRepository;
import com.joaovidal.receitron.domain.model.User;
import com.joaovidal.receitron.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;

    public UserRepositoryAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserEntityMapper::toDomain);
    }

    @Override
    public void save(User user) {
        var entity = userRepository.save(UserEntityMapper.toEntity(user));
    }
}
