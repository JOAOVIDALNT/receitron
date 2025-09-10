package com.joaovidal.receitron.adapter.out.api;

import com.joaovidal.receitron.adapter.in.exception.ApiException;
import com.joaovidal.receitron.adapter.in.web.dto.*;
import com.joaovidal.receitron.domain.model.Recipe;
import com.joaovidal.receitron.domain.port.out.MealdbApiPort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Component
public class MealdbApiAdapter implements MealdbApiPort {

    private final WebClient webClient;
    private final RestClient restClient;

    public MealdbApiAdapter(WebClient.Builder webClientBuilder, RestClient.Builder restClientBuilder) {
        String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
        this.restClient = restClientBuilder.baseUrl(BASE_URL).build();
    }

    @Override
    public List<String> listCultures() {
        try {
            CultureResponse result = restClient.get()
                    .uri("list.php?a=list")
                    .retrieve()
                    .body(CultureResponse.class);

            if (result == null || result.meals() == null) {
                throw new ApiException("Failed to fetch cultures", HttpStatus.BAD_GATEWAY);
            }

            return result.meals().stream().map(CultureObject::culture).toList();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ApiException("Error fetching data from mealdb api", HttpStatus.BAD_GATEWAY);
        } catch (RestClientException e) {
            throw new ApiException("Error communicating with mealdb api", HttpStatus.BAD_GATEWAY);
        } catch (ApiException e) {
            throw new ApiException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            throw new ApiException("Unknow error on mealdb api request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<String> listCategories() {
        try {
            CategoryResponse result = restClient.get()
                    .uri("list.php?c=list")
                    .retrieve()
                    .body(CategoryResponse.class);

            if (result == null || result.meals() == null) {
                throw new ApiException("Failed to fetch categories", HttpStatus.BAD_GATEWAY);
            }

            return result.meals().stream().map(CategoryObject::category).toList();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ApiException("Error fetching data from mealdb api", HttpStatus.BAD_GATEWAY);
        } catch (RestClientException e) {
            throw new ApiException("Error communicating with mealdb api", HttpStatus.BAD_GATEWAY);
        } catch (ApiException e) {
            throw new ApiException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            throw new ApiException("Unknow error on mealdb api request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Recipe getRandomRecipe() {
        try {
            var result =  webClient.get()
                    .uri("random.php")
                    .retrieve()
                    .bodyToMono(RecipeResponse.class)
                    .block();

            if (result == null || result.recipe() == null) {
                throw new ApiException("Failed to fetch recipes", HttpStatus.BAD_GATEWAY);
            }

            return result.recipe().stream().findFirst()
                    .map(x -> new Recipe(x.id(), x.title(), x.category(), x.culture(), x.instructions()))
                    .orElseThrow(() -> new ApiException("No recipe found", HttpStatus.NOT_FOUND));

        } catch (WebClientResponseException e) {
            throw new ApiException("Error fetching data from mealdb api", HttpStatus.BAD_GATEWAY);
        } catch (WebClientRequestException e) {
            throw new ApiException("Error communicating with mealdb api", HttpStatus.BAD_GATEWAY);
        } catch (ApiException e) {
            throw new ApiException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            throw new ApiException("Unknow error on mealdb api request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Recipe> getRecipesByCulture(String culture) {
        try {
            var result = webClient.get()
                    .uri("filter.php?a=" + culture)
                    .retrieve()
                    .bodyToMono(SimpleRecipeResponse.class)
                    .block();

            if (result == null || result.recipe() == null) {
                throw new ApiException("Failed to fetch recipes", HttpStatus.BAD_GATEWAY);
            }

            return result.recipe().stream().map(x -> new Recipe(x.id(), x.title())).toList();

        } catch (WebClientResponseException e) {
            throw new ApiException("Error fetching data from mealdb api", HttpStatus.BAD_GATEWAY);
        } catch (WebClientRequestException e) {
            throw new ApiException("Error communicating with mealdb api", HttpStatus.BAD_GATEWAY);
        } catch (ApiException e) {
            throw new ApiException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            throw new ApiException("Unknow error on mealdb api request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Recipe> getRecipesByCategory(String category) {
        try {
            var result = webClient.get()
                    .uri("filter.php?c=" + category)
                    .retrieve()
                    .bodyToMono(SimpleRecipeResponse.class)
                    .block();

            if (result == null || result.recipe() == null) {
                throw new ApiException("Failed to fetch recipe", HttpStatus.BAD_GATEWAY);
            }

            return result.recipe().stream().map(x -> new Recipe(x.id(), x.title())).toList();

        } catch (WebClientResponseException e) {
            throw new ApiException("Error fetching data from mealdb api", HttpStatus.BAD_GATEWAY);
        } catch (WebClientRequestException e) {
            throw new ApiException("Error communicating with mealdb api", HttpStatus.BAD_GATEWAY);
        } catch (ApiException e) {
            throw new ApiException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            throw new ApiException("Unknow error on mealdb api request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Recipe getRecipeById(int id) {
        try {
            var result = webClient.get()
                    .uri("lookup.php?i=" + id)
                    .retrieve()
                    .bodyToMono(RecipeResponse.class)
                    .block();

            if (result == null || result.recipe() == null) {
                throw new ApiException("Failed to fetch recipe", HttpStatus.BAD_GATEWAY);
            }

            return result.recipe().stream().findFirst()
                    .map(x -> new Recipe(x.id(), x.title(), x.category(), x.culture(), x.instructions()))
                    .orElseThrow(() -> new ApiException("No recipe found", HttpStatus.NOT_FOUND));

        } catch (WebClientResponseException e) {
            throw new ApiException("Error fetching data from mealdb api", HttpStatus.BAD_GATEWAY);
        } catch (WebClientRequestException e) {
            throw new ApiException("Error communicating with mealdb api", HttpStatus.BAD_GATEWAY);
        } catch (ApiException e) {
            throw new ApiException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            throw new ApiException("Unknow error on mealdb api request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
