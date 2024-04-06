FROM --platform=linux/amd64 maven:3-amazoncorretto-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn package -Dmaven.test.skip=true

FROM --platform=linux/amd64 amazoncorretto:21-alpine AS app
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring
COPY --from=build /app/target/todo-backend.jar /app/todo-backend.jar
ENTRYPOINT ["java", "-jar", "/app/todo-backend.jar"]
EXPOSE 8080
