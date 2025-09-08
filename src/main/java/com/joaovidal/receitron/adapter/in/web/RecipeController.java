package com.joaovidal.receitron.adapter.in.web;

import com.joaovidal.receitron.domain.service.RecipeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Recipe", description = "Smart recipe suggestions")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/suggest")
    public ResponseEntity<?> suggestRecipe(@AuthenticationPrincipal UserDetails userDetails) {
        var recipe = recipeService.suggestRecipe(userDetails.getUsername());
        return ResponseEntity.ok(recipe);
    }

    @GetMapping("/weekly-menu")
    public ResponseEntity<?> weeklyMenu(@AuthenticationPrincipal UserDetails userDetails) {
        var recipes = recipeService.weeklyMenu(userDetails.getUsername());
        return ResponseEntity.ok(recipes);
    }
}
