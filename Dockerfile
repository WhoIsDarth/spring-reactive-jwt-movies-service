FROM eclipse-temurin:17-jdk as build

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn

COPY pom.xml .

RUN ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw package

FROM eclipse-temurin:17-jre as production

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080


ENTRYPOINT ["java","-jar","app.jar"]
