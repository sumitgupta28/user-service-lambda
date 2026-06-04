package com.example.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Incoming user payload received in the API Gateway request body.
 *
 * <p>{@code age} is a boxed {@link Integer} so that a missing value is {@code null}
 * (and reported as a validation error) rather than silently defaulting to 0.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String name;
    private String email;
    private Integer age;

    public User() {
    }

    public User(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("age")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{name='" + name + "', email='" + email + "', age=" + age + '}';
    }
}
