FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/opti-life.jar /opti-life/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/opti-life/app.jar"]
