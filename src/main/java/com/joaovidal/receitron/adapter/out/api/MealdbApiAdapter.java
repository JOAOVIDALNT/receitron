package com.joaovidal.receitron.adapter.out.api;

import com.joaovidal.receitron.adapter.in.web.dto.CategoryObject;
import com.joaovidal.receitron.adapter.in.web.dto.CategoryResponse;
import com.joaovidal.receitron.adapter.in.web.dto.CultureObject;
import com.joaovidal.receitron.adapter.in.web.dto.CultureResponse;
import com.joaovidal.receitron.domain.port.out.MealdbApiPort;
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
    public List<String> listCultures() {
        var result =  webClient.get()
                .uri("list.php?a=list")
                .retrieve()
                .bodyToMono(CultureResponse.class)
                .block();
        return result.meals().stream().map(CultureObject::culture).toList();
    }

    @Override
    public List<String> listCategories() {
        var result =  webClient.get()
                .uri("list.php?c=list")
                .retrieve()
                .bodyToMono(CategoryResponse.class)
                .block();
        return result.meals().stream().map(CategoryObject::category).toList();
    }
}
