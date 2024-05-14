FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests


FROM amazoncorretto:17
ARG PROFILE=dev
ARG APP_VERSION=1.0.0
WORKDIR /build
COPY --from=build /build/target/stitch*.jar /app/

EXPOSE 8085

ENV DB_URL=jdbc:postgresql://postgres-sql-stitch:5432/stitch
ENV MAILDEV_URL=localhost

ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} stitch-${JAR_VERSION}.jar