FROM java:8-jre
MAINTAINER Adam Debski adam-debski.com

ADD ./target/eureka-server.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/eureka-server.jar"]

EXPOSE 9999