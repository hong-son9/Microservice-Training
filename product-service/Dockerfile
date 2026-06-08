FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/product-service-1.0.0-SNAPSHOT-exec.jar app.jar

EXPOSE 8082 5005

ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

ENTRYPOINT ["java","-jar","app.jar"]