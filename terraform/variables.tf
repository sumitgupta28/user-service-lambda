variable "aws_region" {
  description = "AWS region to deploy into."
  type        = string
  default     = "us-east-1"
}

variable "function_name" {
  description = "Name of the Lambda function."
  type        = string
  default     = "user-service-lambda"
}

variable "lambda_runtime" {
  description = "Lambda managed runtime."
  type        = string
  default     = "java21"
}

variable "lambda_handler" {
  description = "Fully qualified handler method."
  type        = string
  default     = "com.example.userservice.UserServiceHandler::handleRequest"
}

variable "memory_size" {
  description = "Memory (MB) allocated to the Lambda function."
  type        = number
  default     = 512
}

variable "timeout" {
  description = "Lambda timeout in seconds."
  type        = number
  default     = 30
}

variable "jar_path" {
  description = "Path to the deployable fat JAR produced by `./gradlew shadowJar`."
  type        = string
  default     = "../build/libs/user-service-lambda-all.jar"
}

variable "log_retention_days" {
  description = "CloudWatch log retention in days."
  type        = number
  default     = 14
}
