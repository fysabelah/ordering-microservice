#
# Build stage
#
FROM maven:3.9.6-amazoncorretto-21 AS build

WORKDIR /order-server

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B package -DskipTests

#
# Package stage
#
FROM amazoncorretto:21-alpine-jdk

WORKDIR /order-server

COPY --from=build /order-server/target/*.jar ./order-server.jar

EXPOSE 7078

ENTRYPOINT ["java","-jar","order-server.jar"]