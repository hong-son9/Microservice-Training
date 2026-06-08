FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/api-gateway-1.0.0-SNAPSHOT-exec.jar app.jar

EXPOSE 8080 5005

ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

ENTRYPOINT ["java","-jar","app.jar"]