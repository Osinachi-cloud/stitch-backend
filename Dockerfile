#FROM maven:3.8.7-openjdk-18 AS build
#EXPOSE 8080
#ADD gateway/target/stitch.jar stitch.jar
#ENTRYPOINT ["java", "-jar", "/stitch.jar"]
#WORKDIR /build
#COPY pom.xml .
## RUN mvn dependency:go-offline
#COPY gateway/src ./src
#RUN mvn clean package -DskipTests



FROM maven:3.8.7-openjdk-18 AS build
EXPOSE 8080
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY user/src user/src
RUN mvn clean package -DskipTests




#FROM maven:3.8.7-openjdk-18 AS build
#EXPOSE 8080
#
## Copy the entire parent directory that contains the pom.xml file and the gateway module
##COPY . .
#
#ADD gateway/target/stitch.jar stitch.jar
#
#
## Set the working directory to the root of the project
#WORKDIR /
#
## Run the Maven build command
#RUN mvn clean package -DskipTests
#
## Copy the built JAR file to the root of the Docker image
#COPY gateway/target/stitch.jar stitch.jar
#
## Set the entrypoint for the Docker image
#ENTRYPOINT ["java", "-jar", "/stitch.jar"]



#
#FROM amazoncorretto:17
#ARG PROFILE=dev
#ARG APP_VERSION=1.0.0
#WORKDIR /build
#COPY --from=build /build/target/stitch*.jar /app/
#
##EXPOSE 8085
#
#ENV DB_URL=jdbc:postgresql://postgres-sql-stitch:5432/stitch
#ENV MAILDEV_URL=localhost
#
#ENV ACTIVE_PROFILE=${PROFILE}
#ENV JAR_VERSION=${APP_VERSION}
#
#CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} stitch-${JAR_VERSION}.jar