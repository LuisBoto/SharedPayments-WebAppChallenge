FROM maven:3.8.4-openjdk-11 as builder
WORKDIR /backend/sharedPayments

COPY sharedPayments/pom.xml /backend/sharedPayments
RUN mvn -B clean verify --fail-never
COPY sharedPayments/src /backend/sharedPayments/src
RUN mvn -B package

#########################################################
FROM openjdk:11-jdk-slim
WORKDIR /backend/sharedPayments/
COPY --from=builder /backend/sharedPayments/target/sharedPayments-*.jar /backend/sharedPayments/
EXPOSE 8080
CMD [ "java", "-jar", "sharedPayments-0.2.jar" ]
