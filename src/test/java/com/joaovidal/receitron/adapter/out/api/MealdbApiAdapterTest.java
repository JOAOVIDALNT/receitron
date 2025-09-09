package com.joaovidal.receitron.adapter.out.api;

import com.joaovidal.receitron.adapter.in.web.dto.RecipeObject;
import com.joaovidal.receitron.adapter.in.web.dto.RecipeResponse;
import com.joaovidal.receitron.domain.port.out.MealdbApiPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MealdbApiAdapterTest {


    @InjectMocks
    private MealdbApiAdapter mealdbApiAdapter;
    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        mealdbApiAdapter = new MealdbApiAdapter(webClientBuilder);
    }

    @Test
    void shouldFetchRandomRecipe() {
        var recipeObj = new RecipeObject(1,"teste","teste","teste","teste");
        var recipeResponse = new RecipeResponse(List.of(recipeObj));


    }

}
