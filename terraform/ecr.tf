resource "aws_ecr_repository" "todo_backend" {
  name                 = "todo-backend"
  image_tag_mutability = "MUTABLE" # App Runnerを使っている都合上、MUTABLEにしています。

  image_scanning_configuration {
    scan_on_push = true
  }
}
