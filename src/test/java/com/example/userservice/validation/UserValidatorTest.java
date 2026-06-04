package com.example.userservice.validation;

import com.example.userservice.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserValidatorTest {

    @Test
    void acceptsValidUser() {
        ValidationResult result = UserValidator.validate(new User("Ada Lovelace", "ada@example.com", 36));
        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void rejectsNullUser() {
        ValidationResult result = UserValidator.validate(null);
        assertFalse(result.isValid());
        assertFalse(result.getErrors().isEmpty());
    }

    @Test
    void rejectsBlankName() {
        ValidationResult result = UserValidator.validate(new User("   ", "ada@example.com", 36));
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("name")));
    }

    @Test
    void rejectsTooLongName() {
        String longName = "a".repeat(101);
        ValidationResult result = UserValidator.validate(new User(longName, "ada@example.com", 36));
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("100")));
    }

    @Test
    void rejectsBadEmail() {
        ValidationResult result = UserValidator.validate(new User("Ada", "not-an-email", 36));
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("email")));
    }

    @Test
    void rejectsMissingAge() {
        ValidationResult result = UserValidator.validate(new User("Ada", "ada@example.com", null));
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("age")));
    }

    @Test
    void rejectsOutOfRangeAge() {
        assertFalse(UserValidator.validate(new User("Ada", "ada@example.com", -1)).isValid());
        assertFalse(UserValidator.validate(new User("Ada", "ada@example.com", 200)).isValid());
    }

    @Test
    void collectsAllErrorsAtOnce() {
        ValidationResult result = UserValidator.validate(new User("", "bad", -1));
        assertFalse(result.isValid());
        // name + email + age = three distinct violations
        assertTrue(result.getErrors().size() >= 3);
    }
}
