# App Runnerのオートスケール設定
resource "aws_apprunner_auto_scaling_configuration_version" "web_app" {
  auto_scaling_configuration_name = "todo-backend-autoscaling-conf"

  max_concurrency = 1
  max_size        = 1
  min_size        = 1
}

# App Runnerのインスタンスロール
resource "aws_iam_role" "todo_backend_instance_role" {
  name = "todo-backend-instance-role"

  assume_role_policy = jsonencode(
    {
      Version   = "2012-10-17"
      Statement = [
        {
          Action    = "sts:AssumeRole"
          Effect    = "Allow"
          Principal = {
            Service = "tasks.apprunner.amazonaws.com"
          }
        }
      ]
    }
  )
}

# App Runnerのアクセスロールを作成
resource "aws_iam_role" "app_runner_access_role" {
  name = "todo-backend-access-role"

  assume_role_policy = jsonencode(
    {
      Version   = "2012-10-17"
      Statement = [
        {
          Action    = "sts:AssumeRole"
          Effect    = "Allow"
          Principal = {
            Service = "build.apprunner.amazonaws.com"
          }
        }
      ]
    }
  )
}

# App RunnerのアクセスロールにECRへのアクセス権限を設定
resource "aws_iam_role_policy_attachment" "app_runner_access_role" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSAppRunnerServicePolicyForECRAccess"
  role       = aws_iam_role.app_runner_access_role.name
}

# App Runner Serviceの作成
resource "aws_apprunner_service" "todo_backend" {
  service_name = "todo-backend"

  source_configuration {
    authentication_configuration {
      access_role_arn = aws_iam_role.app_runner_access_role.arn
    }
    image_repository {
      image_configuration {
        port                        = "8080"
        runtime_environment_secrets = {
        }
        runtime_environment_variables = {
          TZ                     = "Asia/Tokyo"
        }
      }
      image_identifier      = "${aws_ecr_repository.todo_backend.repository_url}:latest"
      image_repository_type = "ECR"
    }
    auto_deployments_enabled = false
  }

  auto_scaling_configuration_arn = aws_apprunner_auto_scaling_configuration_version.web_app.arn

  health_check_configuration {
    protocol            = "HTTP"
    path                = "/actuator/health"
    interval            = 5
    timeout             = 1
    healthy_threshold   = 2
    unhealthy_threshold = 2
  }

  instance_configuration {
    cpu               = "512"
    memory            = "1024"
    instance_role_arn = aws_iam_role.todo_backend_instance_role.arn
  }

  network_configuration {
    egress_configuration {
      egress_type       = "DEFAULT"
    }

    ingress_configuration {
      is_publicly_accessible = true
    }
  }

  depends_on = [
    aws_iam_role_policy_attachment.app_runner_access_role,
  ]
}
