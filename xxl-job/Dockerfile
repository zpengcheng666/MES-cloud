FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER jeecgos@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /xxl-job

WORKDIR /xxl-job

EXPOSE 9090

ADD ./target/xxl-job-3.5.0.jar ./

CMD java -Dfile.encoding=utf-8 -Djava.security.egd=file:/dev/./urandom -jar xxl-job-3.5.0.jar

