package com.joaovidal.receitron.adapter.out.persistence.mapper;

import com.joaovidal.receitron.adapter.out.persistence.entity.UserEntity;
import com.joaovidal.receitron.domain.model.User;

import java.util.List;
import java.util.Set;

public class UserEntityMapper {

    public static User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getEmail(), entity.getPassword(), entity.getRoles());
    }

    public static UserEntity toEntity(User user) {
        return new UserEntity(user.getId(), user.getEmail(), user.getPassword(), user.getRoles());
    }

    public static List<User> toDomain(List<UserEntity> entities) {
        return entities.stream()
                .map(UserEntityMapper::toDomain)
                .toList();
    }

}
