FROM openjdk:11-jre-slim
MAINTAINER Adam Debski adam-debski.com

ADD ./target/config-server.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/config-server.jar"]

EXPOSE 8888