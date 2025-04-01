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
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY compose-elastic compose-elastic
COPY compose-postgres compose-postgres
COPY script.sh script.sh
RUN ["chmod", "+x", "script.sh"]
EXPOSE 8000
ENTRYPOINT ["./script.sh"]
#CMD ["./script.sh"]
#ENTRYPOINT ["tail", "-f", "/dev/null"]