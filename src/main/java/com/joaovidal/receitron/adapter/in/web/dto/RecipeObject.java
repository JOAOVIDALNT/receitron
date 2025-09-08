package com.joaovidal.receitron.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RecipeObject(
        @JsonProperty("idMeal")
        int id,
        @JsonProperty("strMeal")
        String title,
        @JsonProperty("strCategory")
        String category,
        @JsonProperty("strArea")
        String culture,
        @JsonProperty("strInstructions")
        String instructions
) {
}
