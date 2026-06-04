package com.example.userservice;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceHandlerTest {

    private final UserServiceHandler handler = new UserServiceHandler();

    private APIGatewayV2HTTPEvent eventWithBody(String body) {
        APIGatewayV2HTTPEvent event = new APIGatewayV2HTTPEvent();
        event.setBody(body);
        return event;
    }

    @Test
    void returns200ForValidUser() {
        TestContext ctx = new TestContext();
        String body = "{\"name\":\"Ada Lovelace\",\"email\":\"ada@example.com\",\"age\":36}";

        APIGatewayV2HTTPResponse response = handler.handleRequest(eventWithBody(body), ctx);

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("User validated"));
        // Validated user details are logged to CloudWatch.
        assertTrue(ctx.logLines.stream().anyMatch(l -> l.contains("validated user")));
    }

    @Test
    void returns400ForMalformedJson() {
        TestContext ctx = new TestContext();

        APIGatewayV2HTTPResponse response = handler.handleRequest(eventWithBody("{not json"), ctx);

        assertEquals(400, response.getStatusCode());
        assertTrue(response.getBody().contains("invalid JSON"));
    }

    @Test
    void returns400ForValidationFailure() {
        TestContext ctx = new TestContext();
        String body = "{\"name\":\"\",\"email\":\"bad\",\"age\":-1}";

        APIGatewayV2HTTPResponse response = handler.handleRequest(eventWithBody(body), ctx);

        assertEquals(400, response.getStatusCode());
        assertTrue(response.getBody().contains("errors"));
    }

    @Test
    void returns400ForEmptyBody() {
        TestContext ctx = new TestContext();

        APIGatewayV2HTTPResponse response = handler.handleRequest(eventWithBody(null), ctx);

        assertEquals(400, response.getStatusCode());
        assertTrue(response.getBody().contains("required"));
    }
}
