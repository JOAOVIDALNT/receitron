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
                Set.of("brazilian"),
                Set.of("beef"),
                Set.of("vegan")
        );

        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(user));

        when(mealdbApiPort.getRecipesByCulture("brazilian"))
                .thenReturn(List.of(new Recipe(1, "feijuca")));

        when(mealdbApiPort.getRecipesByCategory("beef"))
                .thenReturn(List.of(new Recipe(2, "carninha")));

        when(mealdbApiPort.getRecipeById(anyInt()))
                .thenReturn(new Recipe(3, "feijoada", "beef", "brazilian", "cozinhe tudo"));

        when(mealdbApiPort.getRandomRecipe())
                .thenReturn(new Recipe(4, "random", "lamb", "mexican", "misture tudo"));

        var recipes = recipeService.weeklyMenu(email);

        assertNotNull(recipes);
    }

}
