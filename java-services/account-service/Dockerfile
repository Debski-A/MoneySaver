FROM openjdk:11-jre-slim
MAINTAINER Adam Debski adam-debski.com

ADD ./target/account-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/account-service.jar"]

EXPOSE 6000