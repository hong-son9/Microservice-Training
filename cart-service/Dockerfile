FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/cart-service-1.0.0-SNAPSHOT-exec.jar app.jar

EXPOSE 8083 5005

ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

ENTRYPOINT ["java","-jar","app.jar"]