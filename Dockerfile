# Izvēlamies bāzes attēlu ar Maven un JDK
FROM maven:3.8.4-openjdk-17-slim AS build

# Kopējam projektu konteinerā
COPY . /app
WORKDIR /app

# Palaižam Maven, lai būvētu projektu
RUN mvn clean package -DskipTests

# Izvēlamies bāzes attēlu ar JDK, lai palaistu lietotni
FROM openjdk:17.0.2-jdk

# Kopējam jau izveidoto .jar failu no iepriekšējā posma
COPY --from=build /app/target/chat-0.0.1-SNAPSHOT.jar /app/chat-0.0.1-SNAPSHOT.jar

# Atveram portu 8080
EXPOSE 8080

# Palaižam gatavo .jar failu
CMD ["java", "-jar", "/app/chat-0.0.1-SNAPSHOT.jar"]
