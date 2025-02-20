FROM eclipse-temurin:17-jdk-jammy
RUN addgroup demogroup; adduser  --ingroup demogroup --disabled-password demo
USER demo
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","/app.jar"]