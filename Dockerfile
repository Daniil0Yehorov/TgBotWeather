# Используем официальный образ OpenJDK в качестве базового образа
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файлы проекта в рабочую директорию
COPY pom.xml /app/
COPY src /app/src

# Собираем проект с помощью Maven
RUN apt-get update && \
    apt-get install -y maven && \
    mvn -f /app/pom.xml clean package

# Копируем собранный JAR файл в рабочую директорию
COPY target/*.jar app.jar

# Открываем порт, на котором работает приложение
EXPOSE 8080

# Запускаем JAR файл
ENTRYPOINT ["java", "-jar", "app.jar"]