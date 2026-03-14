# ============================================
# Stage 1: Build
# ============================================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy parent POM
COPY pom.xml .

# Copy all module POMs (Maven needs these to resolve the reactor)
COPY gateway/pom.xml ./gateway/
COPY commons/pom.xml ./commons/
COPY user/pom.xml ./user/
COPY currency/pom.xml ./currency/
COPY Order/pom.xml ./order/
COPY product/pom.xml ./product/
COPY payment/pom.xml ./payment/

# Download dependencies (this layer gets cached if POMs don't change)
RUN mvn dependency:go-offline -B

# Copy all source code
COPY gateway/src ./gateway/src
COPY commons/src ./commons/src
COPY user/src ./user/src
COPY currency/src ./currency/src
COPY Order/src ./order/src
COPY product/src ./product/src
COPY payment/src ./payment/src

# Build gateway and all its dependencies (-pl = project list, -am = also make dependencies)
RUN mvn clean package -DskipTests -pl gateway -am

# ============================================
# Stage 2: Runtime
# ============================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from gateway module
COPY --from=build /app/gateway/target/stitch.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]