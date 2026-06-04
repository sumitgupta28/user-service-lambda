# user-service-lambda

A Java 21 AWS Lambda function, fronted by an API Gateway **HTTP API**, that receives
user details over `POST /users`, validates them, and logs the validated details to
CloudWatch. Infrastructure is provisioned with Terraform.

## Architecture

```
Client ──POST /users──▶ API Gateway (HTTP API v2) ──AWS_PROXY──▶ Lambda (java21)
                                                                     │
                                                                     ▼
                                                              CloudWatch Logs
```

- **Handler:** `com.example.userservice.UserServiceHandler::handleRequest`
- **Request body:** `{ "name": "...", "email": "...", "age": 36 }`
- **Validation:** `name` non-blank (≤100 chars), `email` valid format, `age` in `0..150`.
- **Responses:** `200` `{ "message": "User validated", "user": {...} }` on success;
  `400` `{ "errors": [...] }` on bad/invalid input.

## Project layout

```
build.gradle / settings.gradle   # Gradle build (shadow plugin -> fat JAR)
src/main/java/...                 # Lambda handler, model, validator
src/test/java/...                 # JUnit 5 tests
terraform/                        # IAM, Lambda, HTTP API, log group
```

## Prerequisites

- JDK 21
- Terraform >= 1.5
- AWS credentials configured (e.g. `aws configure` / env vars), region `us-east-1`

## Build

```bash
./gradlew clean test shadowJar
```

This runs the unit tests and produces the deployable fat JAR at
`build/libs/user-service-lambda-all.jar` (consumed by Terraform).

## Deploy

```bash
cd terraform
terraform init
terraform apply
```

Terraform packages the JAR, creates the IAM role + log group, the Lambda, and the
HTTP API wiring. The invoke URL is printed as the `api_invoke_url` output.

## Test the endpoint

```bash
URL="$(terraform -chdir=terraform output -raw api_invoke_url)"

# Valid request -> 200
curl -s -X POST "$URL" \
  -H 'Content-Type: application/json' \
  -d '{"name":"Ada Lovelace","email":"ada@example.com","age":36}'

# Invalid request -> 400 with errors
curl -s -X POST "$URL" \
  -H 'Content-Type: application/json' \
  -d '{"name":"","email":"bad","age":-1}'
```

View the logged user details in CloudWatch:

```bash
aws logs tail "$(terraform -chdir=terraform output -raw log_group_name)" --follow
```

## Tear down

```bash
terraform -chdir=terraform destroy
```
