FROM java:8-jre
MAINTAINER Adam Debski adam-debski.com

ADD ./target/config-server.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/config-server.jar"]

EXPOSE 8888