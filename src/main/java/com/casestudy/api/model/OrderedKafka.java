package com.casestudy.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderedKafka(@JsonProperty("product") String product, @JsonProperty("identifier") int identifier) {
}
