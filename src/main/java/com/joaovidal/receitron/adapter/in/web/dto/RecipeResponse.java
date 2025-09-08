package com.joaovidal.receitron.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RecipeResponse(@JsonProperty("meals") List<RecipeObject> recipe) {
}
