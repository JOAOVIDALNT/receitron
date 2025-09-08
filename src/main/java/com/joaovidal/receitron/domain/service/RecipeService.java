package com.joaovidal.receitron.domain.service;

import com.joaovidal.receitron.adapter.in.exception.ApiException;
import com.joaovidal.receitron.domain.model.Recipe;
import com.joaovidal.receitron.domain.port.in.SuggestRecipeUseCase;
import com.joaovidal.receitron.domain.port.out.MealdbApiPort;
import com.joaovidal.receitron.domain.port.out.UserRepositoryPort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RecipeService implements SuggestRecipeUseCase {

    private final MealdbApiPort mealdbApiPort;
    private final UserRepositoryPort userRepositoryPort;

    public RecipeService(MealdbApiPort mealdbApiPort, UserRepositoryPort userRepositoryPort) {
        this.mealdbApiPort = mealdbApiPort;
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Recipe suggestRecipe(String email) {
        var user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        var recipe = new Recipe();
        boolean notFound = true;

        do {
            var randomRecipe = mealdbApiPort.getRandomRecipe();
            if (!user.getRestrictions().contains(randomRecipe.getCategory())) {
                recipe = randomRecipe;
                notFound = false;
            }
        } while (notFound);


        return recipe;
    }
}
