#########################################################
FROM openjdk:11-jdk-slim
WORKDIR /backend/sharedPayments/
COPY ./target/startup.jar /backend/sharedPayments/
EXPOSE 8080
CMD [ "java", "-jar", "startup.jar" ]
