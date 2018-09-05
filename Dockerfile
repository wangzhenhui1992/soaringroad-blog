FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY srcloud-blog-0.0.1-SNAPSHOT.jar  /tmp/app.jar
COPY application-product.properties  /tmp/application-product.properties
COPY application.properties  /tmp/application.properties
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/tmp/app.jar","-Dspring.config.location=file:/tmp/application.properties,file:/tmp/application-product.properties"]
