ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
aws ecr get-login-password --region ap-northeast-1 | docker login --username AWS --password-stdin ${ACCOUNT_ID}.dkr.ecr.ap-northeast-1.amazonaws.com
docker image tag todo-backend:latest ${ACCOUNT_ID}.dkr.ecr.ap-northeast-1.amazonaws.com/todo-backend:latest
docker image push ${ACCOUNT_ID}.dkr.ecr.ap-northeast-1.amazonaws.com/todo-backend:latest
