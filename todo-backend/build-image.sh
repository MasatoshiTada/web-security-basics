docker image pull maven:3-amazoncorretto-21 --platform=linux/amd64
docker image pull amazoncorretto:21-alpine --platform=linux/amd64
docker image build -t todo-backend:latest --platform=linux/amd64 .
