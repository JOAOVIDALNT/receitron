package com.joaovidal.receitron.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CultureObject(@JsonProperty("strArea") String culture) {
}
