package com.joaovidal.receitron.domain.port.out;

import com.joaovidal.receitron.domain.model.Recipe;

import java.util.List;

public interface MealdbApiPort {
    List<String> listCultures();
    List<String> listCategories();
    Recipe getRandomRecipe();
}
