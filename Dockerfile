FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY ./build/libs/ShiftCRM-0.0.1.jar CRM.jar
EXPOSE 8080
CMD java -jar /app/CRM.jar