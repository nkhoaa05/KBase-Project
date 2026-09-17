FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .

COPY pom.xml .

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline -DskipTests

COPY src src

EXPOSE 8080

CMD ["./mvnw", "spring-boot:run"]