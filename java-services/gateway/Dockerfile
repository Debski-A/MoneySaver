FROM openjdk:11-jre-slim
MAINTAINER Adam Debski adam-debski.com

ADD ./target/gateway.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/gateway.jar"]

EXPOSE 4000
