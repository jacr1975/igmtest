FROM openjdk:17.0.2-jdk
WORKDIR /app
VOLUME /tmp
EXPOSE 8080
COPY *.html  build/
COPY properties.xml build/properties.xml
COPY target/igmtest-0.0.1-SNAPSHOT.jar build/igmtest.jar
WORKDIR /app/build
ENTRYPOINT ["java","-jar","igmtest.jar"]