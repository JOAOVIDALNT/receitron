package com.joaovidal.receitron.domain.service;

import com.joaovidal.receitron.adapter.in.exception.ApiException;
import com.joaovidal.receitron.domain.model.Recipe;
import com.joaovidal.receitron.domain.port.in.SuggestRecipeUseCase;
import com.joaovidal.receitron.domain.port.out.MealdbApiPort;
import com.joaovidal.receitron.domain.port.out.UserRepositoryPort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        boolean found = false;

        do {
            var randomRecipe = mealdbApiPort.getRandomRecipe();
            if (!user.getRestrictions().contains(randomRecipe.getCategory())) {
                recipe = randomRecipe;
                found = true;
            }
        } while (!found);


        return recipe;
    }

    @Override
    public List<Recipe> weeklyMenu(String email) {
        var user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        var menu = new HashSet<Recipe>();
        var index = 1;
        int maxAttemps = 40;

        boolean hasAnyFavoriteCulture = !user.getFavoriteCultures().isEmpty();
        boolean hasAnyPreferences = !user.getPreferences().isEmpty();

        do {
            if (hasAnyFavoriteCulture && index % 2 != 0) {
                user.getFavoriteCultures().stream().findAny().ifPresent(x -> {
                    var cultureList = mealdbApiPort.getRecipesByCulture(x); // TODO: CREATE SIMPLERECIPE
                    cultureList.stream().findAny().ifPresent(y -> {
                        var recipe = mealdbApiPort.getRecipeById(y.getId());
                        if (!user.getRestrictions().contains(recipe.getCategory())
                                && menu.stream().noneMatch(z -> z.getId() == y.getId())) {
                            menu.add(recipe);
                        }
                    });
                });
            }
            else {
                var recipe = suggestRecipe(email);
                if (menu.stream().noneMatch(x -> x.getId() == recipe.getId())) {
                    menu.add(suggestRecipe(email));
                }
            }

            if (hasAnyPreferences && index % 2 == 0) {
                user.getPreferences().stream().findAny().ifPresent(x -> {
                    var categoryList = mealdbApiPort.getRecipesByCategory(x);
                    categoryList.stream().findAny().ifPresent(y -> {
                        var recipe = mealdbApiPort.getRecipeById(y.getId());
                        if (!user.getRestrictions().contains(recipe.getCategory())
                        && menu.stream().noneMatch(z -> z.getId() == y.getId())) {
                            menu.add(recipe);
                        }
                    });
                });
            }
            else {
                var recipe = suggestRecipe(email);
                if (menu.stream().noneMatch(x -> x.getId() == recipe.getId())) {
                    menu.add(suggestRecipe(email));
                }
            }


            index++;
            if (index >= maxAttemps) break;
        } while (menu.size() <= 7);

        return menu.stream().toList();
    }
}
