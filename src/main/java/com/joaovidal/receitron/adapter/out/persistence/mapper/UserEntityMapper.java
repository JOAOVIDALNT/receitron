package com.joaovidal.receitron.adapter.out.persistence.mapper;

import com.joaovidal.receitron.adapter.out.persistence.entities.UserEntity;
import com.joaovidal.receitron.domain.model.User;

import java.util.List;

public class UserEntityMapper {

    public static User toDomain(UserEntity entity) {
        return new User(entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRoles(),
                entity.getFavoriteCultures(),
                entity.getPreferences(),
                entity.getRestrictions());
    }

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .preferences(user.getPreferences())
                .restrictions(user.getRestrictions())
                .favoriteCultures(user.getFavoriteCultures())
                .build();
    }

    public static List<User> toDomain(List<UserEntity> entities) {
        return entities.stream()
                .map(UserEntityMapper::toDomain)
                .toList();
    }

}
