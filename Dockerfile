FROM adoptopenjdk:17-jdk-hotspot
LABEL maintainer="stitch"
WORKDIR /app
COPY target/your-application.jar /app/
EXPOSE 8085
CMD ["java", "-jar", "your-application.jar"]
