#基础镜像
FROM java:8
#作者
MAINTAINER demo <1677900582@qq.com>

VOLUME /tmp
#指定配置文件，以及jar包在服务器上的路径
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/data/docker/work/dynamic-data-source/dynamic-data-source.jar"]
#暴露端口
EXPOSE 3030
