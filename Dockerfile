#FROM openjdk:21-jdk-slim
#WORKDIR /app
#COPY . /app
#COPY mvnw pom.xml ./
#RUN ./mvnw dependency:resolve
#RUN ./mvnw package -DskipTests
#EXPOSE 3000
#CMD ["java", "--version", "21", "-jar", "target/bankingapp-0.0.1-SNAPSHOT.jar"]





FROM maven:3.8.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 3000
CMD ["java", "-jar", "/app/app.jar"]