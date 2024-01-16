# Stage 1: Build with Maven
FROM maven:3.6.3 as build
WORKDIR /app
COPY . /app
# RUN mvn clean install
RUN mvn package

# Stage 2: Run with Amazon Corretto JDK image
FROM amazoncorretto:8
WORKDIR /app
COPY --from=build /app/target /app/target
EXPOSE 80

CMD ["java", "-jar", "/app/target/JrhsApp-1.0-SNAPSHOT.jar"]