FROM adoptopenjdk/openjdk8-openj9:alpine-slim
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
EXPOSE 8000
EXPOSE 5701
ENTRYPOINT java $JAVA_OPTS -Xshareclasses -Xquickstart -jar /app.jar
