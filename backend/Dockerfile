FROM maven:3.8.4-openjdk-11 as builder
WORKDIR /backend/parent-project

COPY parent-project/pom.xml /backend/parent-project
RUN mvn -B clean verify --fail-never
COPY parent-project/ /backend/parent-project/
RUN mvn -B package

#########################################################
FROM openjdk:11-jdk-slim
WORKDIR /backend/sharedPayments/
COPY --from=builder /backend/parent-project/startup/target/startup.jar /backend/sharedPayments/
EXPOSE 8080
CMD [ "java", "-jar", "startup.jar" ]
