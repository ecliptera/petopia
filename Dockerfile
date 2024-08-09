FROM amazoncorretto:21

WORKDIR /app

COPY target/petopia-*.jar app/petopia.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "echo Environment Variables: && env && java -jar app/petopia.jar"]