FROM anapsix/alpine-java:8_server-jre_unlimited
MAINTAINER zhp

ENV PARAMS=""

## 创建并进入工作目录
RUN mkdir -p /xxl-job
WORKDIR /xxl-job


## maven 插件构建时得到 buildArgs 种的值
COPY *.jar app.jar

ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

## 暴露端口
EXPOSE 49999

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /xxl-job/app.jar $PARAMS"]