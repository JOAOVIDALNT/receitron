package com.joaovidal.receitron.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SimpleRecipeObject(
        @JsonProperty("idMeal")
        int id,
        @JsonProperty("strMeal")
        String title
) {
}
