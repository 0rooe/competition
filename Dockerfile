# 使用 eclipse-temurin 官方稳定镜像 (替代废弃的 openjdk:8-jdk-alpine)
FROM eclipse-temurin:8-jre-alpine

# 设置作者信息 (可选)
LABEL maintainer="springbootpx13e-docker"

# 设置工作目录
WORKDIR /app

# 将构建好的 jar 包复制到镜像中
# 注意：这里假设您的 jar 包生成后叫 springbootpx13e-0.0.1-SNAPSHOT.jar
# 如果名字不同，请修改这里，或者在 pom.xml <build><finalName> 中指定名字
COPY target/springbootpx13e-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口 (与 application.yml 中的 server.port 保持一致)
EXPOSE 8080

# 启动命令
# 下面的参数是为了在 Docker 容器（特别是 Alpine）中优化 Java 启动和字体支持
# add -Djava.security.egd=file:/dev/./urandom 用于加快随机数生成
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
