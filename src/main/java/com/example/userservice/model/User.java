package com.example.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Incoming user payload received in the API Gateway request body.
 *
 * <p>{@code age} is a boxed {@link Integer} so that a missing value is {@code null}
 * (and reported as a validation error) rather than silently defaulting to 0.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class User {

    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;

    @JsonProperty("age")
    private Integer age;




}
