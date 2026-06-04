output "api_invoke_url" {
  description = "Full URL for POST /users."
  value       = "${aws_apigatewayv2_stage.default.invoke_url}/users"
}

output "lambda_function_arn" {
  description = "ARN of the deployed Lambda function."
  value       = aws_lambda_function.user_service.arn
}

output "log_group_name" {
  description = "CloudWatch log group where user details are logged."
  value       = aws_cloudwatch_log_group.lambda.name
}
