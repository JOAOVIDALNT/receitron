package com.joaovidal.receitron.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryObject(@JsonProperty("strCategory") String category) {
}
