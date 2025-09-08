package com.joaovidal.receitron.domain.service;

import com.joaovidal.receitron.domain.model.Recipe;
import com.joaovidal.receitron.domain.model.User;
import com.joaovidal.receitron.domain.port.out.MealdbApiPort;
import com.joaovidal.receitron.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private MealdbApiPort mealdbApiPort;

    @InjectMocks
    private RecipeService recipeService;

    String email = "tester@email.com";
    @Test
    void shouldProvideARandomRecipe() {
        User user = new User(UUID.randomUUID(),email, "password", Set.of("USER"));
        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(user));

        when(mealdbApiPort.getRandomRecipe())
                .thenReturn(new Recipe(1,"teste","teste","teste","teste"));

        var recipe = recipeService.suggestRecipe(email);

        assertEquals(1, recipe.getId());
        verify(userRepositoryPort, times(1)).findByEmail(email);
        verify(mealdbApiPort, times(1)).getRandomRecipe();
    }

    @Test
    void shouldProvideAWeeklyMenu() {
        User user = new User(
                UUID.randomUUID(),
                email,
                "password",
                Set.of("USER"),
                Set.of("Brazilian"),
                Set.of("Beef"),
                Set.of("Vegan")
        );

        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(user));

        when(mealdbApiPort.getRecipesByCulture("Brazilian"))
                .thenReturn(List.of(new Recipe(1, "feijuca")));

        when(mealdbApiPort.getRecipesByCategory("Beef"))
                .thenReturn(List.of(new Recipe(2, "carninha")));

        when(mealdbApiPort.getRecipeById(anyInt()))
                .thenReturn(new Recipe(3, "Feijoada", "Beef", "Brazilian", "cozinhe tudo"));

        when(mealdbApiPort.getRandomRecipe())
                .thenReturn(new Recipe(4, "random", "Lamb", "Mexican", "misture tudo"));

        var recipes = recipeService.weeklyMenu(email);

        assertNotNull(recipes);
    }

}
