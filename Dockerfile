FROM openjdk:8-jdk-alpine
WORKDIR /
COPY dkserver-back-1.0-SNAPSHOT.jar //
ENV TZ='Asia/Shanghai'
ENV AppPort=8200
ENTRYPOINT ["java","-Dfile.encoding=UTF-8","-Duser.timezone=GMT+8","-jar","dkserver-back-1.0-SNAPSHOT.jar","--server.port=${AppPort}"]
