FROM openjdk:11-jre-slim
MAINTAINER Adam Debski adam-debski.com

ADD ./target/calculation-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/calculation-service.jar"]

EXPOSE 6001