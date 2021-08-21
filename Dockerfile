FROM maven:3-jdk-8-alpine as builder
COPY . /tmp
WORKDIR /tmp
RUN mvn clean install -DskipTests

FROM openjdk:8-jdk-alpine
COPY --from=builder /tmp/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 9091
