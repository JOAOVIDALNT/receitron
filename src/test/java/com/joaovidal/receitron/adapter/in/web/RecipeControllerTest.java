package com.joaovidal.receitron.adapter.in.web;

import com.joaovidal.receitron.domain.model.Recipe;
import com.joaovidal.receitron.domain.service.RecipeService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test") // Pra não executar o filtro devido as proteçoes relacionadas ao h2
public class RecipeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeService recipeService;

    String email = "tester@email.com";
    String password = "supersecretpass";

    @Test
    @WithMockUser(username = "tester@email.com", roles = {"USER"})
    void shouldSuggestRecipe() throws Exception{
        Recipe recipe = new Recipe(1,"teste","teste","teste","teste");

        when(recipeService.suggestRecipe(email)).thenReturn(recipe);

        mockMvc.perform(get("/recipe/suggest"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "tester@email.com", roles = {"USER"})
    void shouldGenerateAWeeklyMenu() throws Exception{
        Recipe recipe = new Recipe(1,"teste","teste","teste","teste");

        when(recipeService.weeklyMenu(email)).thenReturn(
                List.of(recipe)
        );

        mockMvc.perform(get("/recipe/weekly-menu"))
                .andExpect(status().isOk());
    }

    //TODO: TEST EXCEPTIONS HERE AND AT SERVICETEST
}
