FROM openjdk:11-jre-slim
MAINTAINER Adam Debski adam-debski.com

ADD ./target/eureka-server.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/eureka-server.jar"]

EXPOSE 9999