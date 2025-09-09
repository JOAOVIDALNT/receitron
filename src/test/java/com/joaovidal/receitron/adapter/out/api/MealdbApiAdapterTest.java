package com.joaovidal.receitron.adapter.out.api;

import com.joaovidal.receitron.adapter.in.web.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MealdbApiAdapterTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private RestClient.Builder restClientBuilder;
    @Mock
    private WebClient.RequestHeadersUriSpec wcRequestHeadersUriSpecMock;
    @Mock
    private WebClient.RequestHeadersSpec wcRequestHeadersSpecMock;
    @Mock
    private WebClient.ResponseSpec wcResponseSpecMock;
    @Mock
    private RestClient restClient;
    @Mock
    private RestClient.RequestHeadersUriSpec rcRequestHeadersUriSpecMock;
    @Mock
    private RestClient.RequestHeadersSpec rcRequestHeadersSpecMock;
    @Mock
    private RestClient.ResponseSpec rcResponseSpecMock;

    private MealdbApiAdapter mealdbApiAdapter;

    @BeforeEach
    void setup() {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(restClientBuilder.baseUrl(anyString())).thenReturn(restClientBuilder);
        when(restClientBuilder.build()).thenReturn(restClient);
        mealdbApiAdapter = new MealdbApiAdapter(webClientBuilder, restClientBuilder);
    }

    @Test
    void shouldFetchRandomRecipe() {
        var recipeObj = new RecipeObject(1,"teste","teste","teste","teste");
        var recipeResponse = new RecipeResponse(List.of(recipeObj));

        when(webClient.get()).thenReturn(wcRequestHeadersUriSpecMock);
        when(wcRequestHeadersUriSpecMock.uri("random.php")).thenReturn(wcRequestHeadersSpecMock);
        when(wcRequestHeadersSpecMock.retrieve()).thenReturn(wcResponseSpecMock);
        when(wcResponseSpecMock.bodyToMono(RecipeResponse.class)).thenReturn(Mono.just(recipeResponse));

        var response = mealdbApiAdapter.getRandomRecipe();

        assertEquals(response.getCategory(), recipeObj.category());
    }

    @Test
    void shouldFetchRecipesByCulture() {

        String title = "title";
        SimpleRecipeResponse simpleRecipeResponse =
                new SimpleRecipeResponse(List.of(new SimpleRecipeObject(1,title)));

        String culture = "brazilian";
        when(webClient.get()).thenReturn(wcRequestHeadersUriSpecMock);
        when(wcRequestHeadersUriSpecMock.uri("filter.php?a="+culture)).thenReturn(wcRequestHeadersSpecMock);
        when(wcRequestHeadersSpecMock.retrieve()).thenReturn(wcResponseSpecMock);
        when(wcResponseSpecMock.bodyToMono(SimpleRecipeResponse.class)).thenReturn(Mono.just(simpleRecipeResponse));

        var response = mealdbApiAdapter.getRecipesByCulture(culture);

        assertTrue(response.size() == 1);
        assertEquals(title, response.stream().findFirst().get().getTitle());
    }

    @Test
    void shouldFetchRecipesByCategory() {

        String title = "title";
        SimpleRecipeResponse simpleRecipeResponse =
                new SimpleRecipeResponse(List.of(new SimpleRecipeObject(1, title)));

        String category = "brazilian";
        when(webClient.get()).thenReturn(wcRequestHeadersUriSpecMock);
        when(wcRequestHeadersUriSpecMock.uri("filter.php?c="+category)).thenReturn(wcRequestHeadersSpecMock);
        when(wcRequestHeadersSpecMock.retrieve()).thenReturn(wcResponseSpecMock);
        when(wcResponseSpecMock.bodyToMono(SimpleRecipeResponse.class)).thenReturn(Mono.just(simpleRecipeResponse));

        var response = mealdbApiAdapter.getRecipesByCategory(category);

        assertTrue(response.size() == 1);
        assertEquals(title, response.stream().findFirst().get().getTitle());
    }

    @Test
    void shouldFetchRecipesById() {

        int id = 13;
        RecipeResponse recipeResponse = new RecipeResponse(List.of(new RecipeObject(
                id, "title", "category", "culture", "instructions"
        )));

        when(webClient.get()).thenReturn(wcRequestHeadersUriSpecMock);
        when(wcRequestHeadersUriSpecMock.uri("lookup.php?i="+id)).thenReturn(wcRequestHeadersSpecMock);
        when(wcRequestHeadersSpecMock.retrieve()).thenReturn(wcResponseSpecMock);
        when(wcResponseSpecMock.bodyToMono(RecipeResponse.class)).thenReturn(Mono.just(recipeResponse));

        var response = mealdbApiAdapter.getRecipeById(id);

        assertEquals(id, response.getId());
    }

    @Test
    void shouldFetchCultureList() {
        CultureResponse cultureResponse =
                new CultureResponse(List.of(new CultureObject("brazilian")));

        when(restClient.get()).thenReturn(rcRequestHeadersUriSpecMock);
        when(rcRequestHeadersUriSpecMock.uri("list.php?a=list")).thenReturn(rcRequestHeadersSpecMock);
        when(rcRequestHeadersSpecMock.retrieve()).thenReturn(rcResponseSpecMock);
        when(rcResponseSpecMock.body(CultureResponse.class)).thenReturn(cultureResponse);

        List<String> cultures = mealdbApiAdapter.listCultures();

        assertEquals(List.of("brazilian"), cultures);
    }

    @Test
    void shouldFetchCategoryList() {
        CategoryResponse categoryResponse =
                new CategoryResponse(List.of(new CategoryObject("vegan")));

        when(restClient.get()).thenReturn(rcRequestHeadersUriSpecMock);
        when(rcRequestHeadersUriSpecMock.uri("list.php?c=list")).thenReturn(rcRequestHeadersSpecMock);
        when(rcRequestHeadersSpecMock.retrieve()).thenReturn(rcResponseSpecMock);
        when(rcResponseSpecMock.body(CategoryResponse.class)).thenReturn(categoryResponse);

        List<String> categories = mealdbApiAdapter.listCategories();

        assertEquals(List.of("vegan"), categories);
    }

    // TODO: TEST ERRORS
}
