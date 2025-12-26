# ==========================================
# Stage 1: Build Admin Frontend (Vue.js)
# ==========================================
# 使用 Node 12 环境 (为了兼容 node-sass 4.x)
FROM node:12 as frontend-builder

WORKDIR /app-frontend

# 设置 npm 淘宝镜像加速 (极大提升国内构建速度)
RUN npm config set registry https://registry.npmmirror.com

# 单独复制 package 配置文件，利用 Docker 缓存层
COPY src/main/resources/admin/admin/package.json src/main/resources/admin/admin/package-lock.json ./

# 安装前端依赖
RUN npm install

# 复制前端项目所有源代码
COPY src/main/resources/admin/admin/ .

# 执行构建 -> 生成 dist 目录
RUN npm run build

# ==========================================
# Stage 2: Build Backend (Java Spring Boot)
# ==========================================
FROM maven:3.8-jdk-8 as backend-builder

WORKDIR /app-backend

# 1. 复制 pom.xml 并预下载依赖 (利用缓存)
COPY pom.xml .
# 加上 -B 参数静默下载，减少日志输出
RUN mvn -B dependency:go-offline

# 2. 复制后端所有源码
COPY src ./src

# 3. 【关键步骤】将 Stage 1 构建好的前端资源注入到正确的位置
# 这相当于在本地执行了 copy 操作
COPY --from=frontend-builder /app-frontend/dist ./src/main/resources/admin/admin/dist

# 4. 执行 Maven 打包 (跳过测试)
RUN mvn clean package -DskipTests

# ==========================================
# Stage 3: Final Runtime Image
# ==========================================
FROM eclipse-temurin:8-jre-alpine

LABEL maintainer="springbootpx13e-docker"

WORKDIR /app

# 从 Stage 2 获取打包好的 Jar 文件
COPY --from=backend-builder /app-backend/target/springbootpx13e-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
