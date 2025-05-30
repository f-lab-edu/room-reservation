# Dockerfile
FROM eclipse-temurin:17-jdk-alpine

# JAR 파일 복사 (bootJar 파일명 고정 가정)
COPY yeogi-customer/build/libs/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
