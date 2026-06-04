package com.example.userservice;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal {@link Context} stub for unit tests. Captures log output so tests can assert on it.
 */
class TestContext implements Context {

    final List<String> logLines = new ArrayList<>();

    private final LambdaLogger logger = new LambdaLogger() {
        @Override
        public void log(String message) {
            logLines.add(message);
        }

        @Override
        public void log(byte[] message) {
            logLines.add(new String(message));
        }
    };

    @Override
    public LambdaLogger getLogger() {
        return logger;
    }

    // --- Unused Context methods ---
    @Override public String getAwsRequestId() { return "test-request-id"; }
    @Override public String getLogGroupName() { return "test-log-group"; }
    @Override public String getLogStreamName() { return "test-log-stream"; }
    @Override public String getFunctionName() { return "user-service-lambda"; }
    @Override public String getFunctionVersion() { return "$LATEST"; }
    @Override public String getInvokedFunctionArn() { return "arn:aws:lambda:us-east-1:000000000000:function:user-service-lambda"; }
    @Override public CognitoIdentity getIdentity() { return null; }
    @Override public ClientContext getClientContext() { return null; }
    @Override public int getRemainingTimeInMillis() { return 30000; }
    @Override public int getMemoryLimitInMB() { return 512; }
}
