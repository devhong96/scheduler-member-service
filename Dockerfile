FROM amazoncorretto:17-alpine
COPY ./build/libs/member-service.jar member-service.jar
ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=200", "-jar", "member-service.jar"]