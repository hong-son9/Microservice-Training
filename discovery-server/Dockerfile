FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/discovery-server-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 8761 5005

ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

ENTRYPOINT ["java","-jar","app.jar"]