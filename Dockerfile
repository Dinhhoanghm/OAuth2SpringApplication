FROM maven:3.8.3-openjdk-17 AS build
# Set the working directory inside the container
WORKDIR /app
# Copy the rest of the application source code
COPY . .
# Use a mount to cache Maven dependencies
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline
# Build the application
RUN --mount=type=cache,target=/root/.m2 mvn clean install -am -DskipTests

# Create a new image with a JRE base for deployment
FROM openjdk:17.0-jdk

ENV TZ="Asia/Ho_Chi_Minh"
RUN rm -rf /var/cache/apk/*

# Set the working directory inside the container
WORKDIR /app

# Copy the built artifact from the previous stage to the container
COPY --from=build /app/target/*.jar ./aivhub-application.jar

ENTRYPOINT exec java $JAVA_OPTS -jar $JAVA_ARGS aivhub-application.jar