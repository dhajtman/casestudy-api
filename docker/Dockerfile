#FROM eclipse-temurin:17-jdk-jammy
#FROM docker:stable-dind
FROM docker:dind

# Install dependencies
RUN apk add --no-cache &&\
        wget -O /etc/apk/keys/amazoncorretto.rsa.pub https://apk.corretto.aws/amazoncorretto.rsa.pub && \
        echo "https://apk.corretto.aws" >> /etc/apk/repositories && \
        apk update &&\
        apk add amazon-corretto-17

# Set JAVA_HOME environment variable
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk
ENV PATH="$JAVA_HOME/bin:${PATH}"

WORKDIR /app
#RUN addgroup demogroup; adduser  --ingroup demogroup --disabled-password demo
#USER demo
ARG JAR_FILE=../target/*.jar
COPY ${JAR_FILE} app.jar
RUN ["pwd"]
COPY ../compose-elastic compose-elastic
COPY ../compose-kafka compose-kafka
COPY ../compose-postgres compose-postgres
COPY ./docker/startup_script.sh startup_script.sh
RUN ["chmod", "+x", "startup_script.sh"]
EXPOSE 8000
ENTRYPOINT ["./startup_script.sh"]
#CMD ["./startup_script.sh"]
#ENTRYPOINT ["tail", "-f", "/dev/null"]