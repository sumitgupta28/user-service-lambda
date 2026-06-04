package com.example.userservice.validation;

import com.example.userservice.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validates incoming {@link User} payloads. Collects all violations so the
 * caller can return a complete error list in one response.
 */
public final class UserValidator {

    private static final int MAX_NAME_LENGTH = 100;
    private static final int MIN_AGE = 0;
    private static final int MAX_AGE = 150;

    // Pragmatic email check: one local part, one domain, one dot in the domain, no spaces.
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private UserValidator() {
    }

    public static ValidationResult validate(User user) {
        List<String> errors = new ArrayList<>();

        if (user == null) {
            errors.add("request body is missing or empty");
            return ValidationResult.of(errors);
        }

        validateName(user.getName(), errors);
        validateEmail(user.getEmail(), errors);
        validateAge(user.getAge(), errors);

        return ValidationResult.of(errors);
    }

    private static void validateName(String name, List<String> errors) {
        if (name == null || name.isBlank()) {
            errors.add("name is required and must not be blank");
        } else if (name.trim().length() > MAX_NAME_LENGTH) {
            errors.add("name must be at most " + MAX_NAME_LENGTH + " characters");
        }
    }

    private static void validateEmail(String email, List<String> errors) {
        if (email == null || email.isBlank()) {
            errors.add("email is required");
        } else if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            errors.add("email is not a valid email address");
        }
    }

    private static void validateAge(Integer age, List<String> errors) {
        if (age == null) {
            errors.add("age is required");
        } else if (age < MIN_AGE || age > MAX_AGE) {
            errors.add("age must be between " + MIN_AGE + " and " + MAX_AGE);
        }
    }
}
