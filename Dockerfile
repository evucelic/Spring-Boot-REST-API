FROM gradle:7.3.3-jdk17 AS build
WORKDIR /SpringBootREST
COPY . .
RUN ./gradlew clean build -x test

FROM openjdk:17-jdk-alpine
WORKDIR /SpringBootREST
COPY --from=build /SpringBootREST/build/libs/SpringBootREST-0.0.1-SNAPSHOT.jar /SpringBootREST/SpringBootREST-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "SpringBootREST-0.0.1-SNAPSHOT.jar"]
