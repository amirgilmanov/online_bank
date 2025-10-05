FROM openjdk:21-ea-1-jdk-slim
WORKDIR /app
COPY target/online_bank-1.1.0.jar /app/online_bank.jar
ENTRYPOINT ["java", "-jar", "online_bank.jar"]
