package com.example.userservice;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.example.userservice.model.User;
import com.example.userservice.validation.UserValidator;
import com.example.userservice.validation.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Lambda entry point. Triggered by an API Gateway HTTP API (v2) on {@code POST /users}.
 *
 * <p>Parses the JSON request body into a {@link User}, validates it, logs the validated
 * details to CloudWatch, and returns a 200 on success or 400 on bad input.
 */
public class UserServiceHandler
        implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

    // ObjectMapper is thread-safe and expensive to build; create once and reuse across warm invocations.
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Map<String, String> JSON_HEADERS =
            Map.of("Content-Type", "application/json");

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context) {
        LambdaLogger logger = context.getLogger();

        String body = event == null ? null : event.getBody();

        User user;
        try {
            if (body == null || body.isBlank()) {
                logger.log("WARN: received request with empty body");
                return jsonResponse(400, Map.of("errors", List.of("request body is required")));
            }
            user = MAPPER.readValue(body, User.class);
        } catch (Exception e) {
            logger.log("WARN: failed to parse request body as JSON: " + e.getMessage());
            return jsonResponse(400, Map.of("errors", List.of("invalid JSON in request body")));
        }

        ValidationResult result = UserValidator.validate(user);
        if (!result.isValid()) {
            logger.log("WARN: user validation failed: " + result.getErrors());
            return jsonResponse(400, Map.of("errors", result.getErrors()));
        }

        // Per requirement: log the validated user details to CloudWatch.
        logger.log("INFO: validated user -> " + user);

        Map<String, Object> success = new LinkedHashMap<>();
        success.put("message", "User validated");
        success.put("user", user);
        return jsonResponse(200, success);
    }

    private APIGatewayV2HTTPResponse jsonResponse(int statusCode, Object payload) {
        String serialized;
        try {
            serialized = MAPPER.writeValueAsString(payload);
        } catch (Exception e) {
            // Serialization of our own controlled payloads should never fail; fall back defensively.
            serialized = "{\"errors\":[\"internal serialization error\"]}";
            statusCode = 500;
        }
        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(statusCode)
                .withHeaders(JSON_HEADERS)
                .withBody(serialized)
                .build();
    }
}
