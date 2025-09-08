package com.joaovidal.receitron.domain.service;

import com.joaovidal.receitron.adapter.in.exception.ApiException;
import com.joaovidal.receitron.domain.model.User;
import com.joaovidal.receitron.domain.port.in.FindUserUseCase;
import com.joaovidal.receitron.domain.port.in.UpdateUserUseCase;
import com.joaovidal.receitron.domain.port.out.MealdbApiPort;
import com.joaovidal.receitron.domain.port.out.UserRepositoryPort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements FindUserUseCase, UpdateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final MealdbApiPort mealdbApiPort;

    public UserService(UserRepositoryPort userRepositoryPort, MealdbApiPort mealdbApiPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.mealdbApiPort = mealdbApiPort;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        var user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        return Optional.of(user);
    }

    @Override
    public User addCultures(String email, List<String> cultures) {
        var user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        var avaliableCultures = mealdbApiPort.listCultures();

        cultures.forEach(x -> {
            if (!avaliableCultures.contains(x)) {
                throw new ApiException(String.format("Sorry, culture %s doesn't have data", x), HttpStatus.BAD_REQUEST);
            }
            user.getFavoriteCultures().add(x);
        });

        userRepositoryPort.save(user);
        return user;
    }

    @Override
    public User addPreferences(String email, List<String> preferences) {
        var user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        var avaliableCategories = mealdbApiPort.listCategories();

        preferences.forEach(x -> {
            if (!avaliableCategories.contains(x)) {
                throw new ApiException(String.format("Sorry, category %s doesn't have data", x), HttpStatus.BAD_REQUEST);
            }
            user.getPreferences().add(x);
        });

        userRepositoryPort.save(user);
        return user;
    }

    @Override
    public User addRestrictions(String email, List<String> restrictions) {
        var user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        var avaliableCategories = mealdbApiPort.listCategories();

        restrictions.forEach(x -> {
            if (!avaliableCategories.contains(x)) {
                throw new ApiException(String.format("Sorry, category %s doesn't have data", x), HttpStatus.BAD_REQUEST);
            }
            user.getRestrictions().add(x);
        });

        userRepositoryPort.save(user);
        return user;
    }
}
