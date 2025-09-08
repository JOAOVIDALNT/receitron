package com.joaovidal.receitron.domain.port.in;

import com.joaovidal.receitron.domain.model.Recipe;

import java.util.List;

public interface SuggestRecipeUseCase {
    Recipe suggestRecipe(String email);
    List<Recipe> weeklyMenu(String email);
}
