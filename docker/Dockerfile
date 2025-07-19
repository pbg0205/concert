# 베이스 이미지
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

EXPOSE 8080

# JAR 파일 복사
COPY /build/libs/concert-0.0.1-SNAPSHOT.jar app.jar

# 프로파일 설정하여 실행
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=local"]
