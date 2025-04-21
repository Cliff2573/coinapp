# 使用 OpenJDK 17 作為基底映像檔
FROM openjdk:17-jdk-slim

# 設定工作目錄
WORKDIR /app

# 複製 Spring Boot 應用程式的 JAR 檔案到容器中
COPY target/coinapp-0.0.1-SNAPSHOT.jar app.jar

# 暴露應用程式的埠號
EXPOSE 8080

# 啟動 Spring Boot 應用程式
CMD ["java", "-jar", "app.jar"]