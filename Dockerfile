FROM openjdk:8u181
WORKDIR /
COPY bootstrap.yml /bootstrap.yml
COPY dkserver-back-1.0-SNAPSHOT.jar //
ENV TZ='Asia/Shanghai'
ENTRYPOINT ["java","-Dfile.encoding=UTF-8","-Duser.timezone=GMT+8","-jar","dkserver-back-1.0-SNAPSHOT.jar","--server.port=0","--spring.config.location=/bootstrap.yml"]