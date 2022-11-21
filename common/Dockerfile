FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER Yu Gaoheng

RUN mkdir -p /wind/server
RUN mkdir -p /wind/server/logs
RUN mkdir -p /wind/server/temp

WORKDIR /wind/server

ENV SERVER_PORT=8080

EXPOSE ${SERVER_PORT}

ADD ./target/wind-admin.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=${SERVER_PORT}","-jar", "app.jar"]
