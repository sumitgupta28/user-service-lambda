package com.example.userservice.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * Outcome of validating a {@link com.example.userservice.model.User}.
 * Carries the overall validity flag plus every error encountered (not just the first).
 */
@Getter
public final class ValidationResult {

    private final boolean valid;
    private final List<String> errors;

    private ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        this.errors = Collections.unmodifiableList(errors);
    }

    public static ValidationResult of(List<String> errors) {
        return new ValidationResult(errors.isEmpty(), errors);
    }

}
