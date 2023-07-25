FROM openjdk:20

RUN ./mvnw install -DskipTests

COPY target/web_shop-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]