FROM openjdk:8-jdk-alpine
WORKDIR /
COPY dkserver-back-1.0-SNAPSHOT.jar //
ENV TZ='Asia/Shanghai'
ENV AppPort=8100
#RUN apk --update add curl bash ttf-dejavu && rm -rf /var/cache/apk/*
ENTRYPOINT ["java","-Dfile.encoding=UTF-8","-Duser.timezone=GMT+8","-jar","dkserver-back-1.0-SNAPSHOT.jar","--server.port=${AppPort}"]
