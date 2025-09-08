package com.joaovidal.receitron.domain.port.in;

import com.joaovidal.receitron.domain.model.Recipe;

public interface SuggestRecipeUseCase {
    Recipe suggestRecipe(String email);
}
