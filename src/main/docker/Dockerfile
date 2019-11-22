FROM openjdk:8
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8000
ENTRYPOINT ["java","-jar","/app.jar"]
