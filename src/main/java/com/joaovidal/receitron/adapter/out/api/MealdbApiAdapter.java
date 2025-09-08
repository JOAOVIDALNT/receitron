package com.joaovidal.receitron.adapter.out.api;

import com.joaovidal.receitron.adapter.in.exception.ApiException;
import com.joaovidal.receitron.adapter.in.web.dto.*;
import com.joaovidal.receitron.domain.model.Recipe;
import com.joaovidal.receitron.domain.port.out.MealdbApiPort;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class MealdbApiAdapter implements MealdbApiPort {

    private final WebClient webClient;

    public MealdbApiAdapter(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://www.themealdb.com/api/json/v1/1/").build();
    }

    @Override
    @Cacheable(cacheNames = "cultures")
    public List<String> listCultures() {
        var result =  webClient.get()
                .uri("list.php?a=list")
                .retrieve()
                .bodyToMono(CultureResponse.class)
                .block();

        if (result == null || result.meals() == null) {
            throw new ApiException("Failed to fetch cultures", HttpStatus.BAD_GATEWAY);
        }

        return result.meals().stream().map(CultureObject::culture).toList();
    }

    @Override
    @Cacheable(cacheNames = "categories")
    public List<String> listCategories() {
        var result =  webClient.get()
                .uri("list.php?c=list")
                .retrieve()
                .bodyToMono(CategoryResponse.class)
                .block();

        if (result == null || result.meals() == null) {
            throw new ApiException("Failed to fetch categories", HttpStatus.BAD_GATEWAY);
        }

        return result.meals().stream().map(CategoryObject::category).toList();
    }

    @Override
    public Recipe getRandomRecipe() {
        var result =  webClient.get()
                .uri("random.php")
                .retrieve()
                .bodyToMono(RecipeResponse.class)
                .block();

        if (result == null || result.recipe() == null) {
            throw new ApiException("Failed to fetch recipe", HttpStatus.BAD_GATEWAY);
        }

        return result.recipe().stream().findFirst()
                .map(x -> new Recipe(x.id(), x.title(), x.category(), x.culture(), x.instructions()))
                .orElseThrow(() -> new ApiException("No recipe found", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Recipe> getRecipesByCulture(String culture) {
        var result =  webClient.get()
                .uri("filter.php?a="+culture)
                .retrieve()
                .bodyToMono(SimpleRecipeResponse.class)
                .block();

        if (result == null || result.recipe() == null) {
            throw new ApiException("Failed to fetch recipe", HttpStatus.BAD_GATEWAY);
        }

        return result.recipe().stream().map(x -> new Recipe(x.id(), x.title())).toList();
    }

    @Override
    public List<Recipe> getRecipesByCategory(String category) {
        var result =  webClient.get()
                .uri("filter.php?c="+category)
                .retrieve()
                .bodyToMono(SimpleRecipeResponse.class)
                .block();

        if (result == null || result.recipe() == null) {
            throw new ApiException("Failed to fetch recipe", HttpStatus.BAD_GATEWAY);
        }

        return result.recipe().stream().map(x -> new Recipe(x.id(), x.title())).toList();
    }

    @Override
    public Recipe getRecipeById(int id) {
        return null;
    }
}
