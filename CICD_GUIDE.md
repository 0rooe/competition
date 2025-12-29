# Spring Boot 项目 CI/CD全流程搭建指南

本文档总结了一个基于 Spring Boot + Vue 的前后端分离项目（通过 Maven 打包在一起）如何实现从代码提交到 Docker 容器自动部署的全流程。

## 1. 核心架构

*   **CI (持续集成)**: 使用 **GitHub Actions**。虽然项目包含 Java 后端和 Vue 前端，但为了简化流程和消除环境依赖，采用了 **Docker 多阶段构建**。即把编译前端 (npm) 和后端 (mvn) 的工作全部交给 Dockerfile 完成。
*   **CD (持续部署)**: 使用 **GitHub Actions + SSH**。构建好的镜像推送到 GitHub Container Registry (ghcr.io)，然后通过 SSH 远程控制服务器拉取新镜像并重启容器。

---

## 2. 准备工作

### 2.1 服务器端 (Linux)
1.  **安装 Docker & Docker Compose**: 确保服务器具备运行容器的能力。
2.  **登录镜像仓库**:
    *   在 GitHub 生成一个 Personal Access Token (权限选 `write:packages`, `read:packages`)。
    *   在服务器执行登录（只需一次）：
        ```bash
        echo "your_ghp_token" | docker login ghcr.io -u your_github_username --password-stdin
        ```
3.  **准备配置文件**:
    *   在服务器创建目录（如 `/opt/project`）。
    *   上传生产环境的 `docker-compose.yml` (其中的 `image` 指向 ghcr.io 的远程镜像，而非本地 build)。

### 2.2 GitHub 仓库端
配置 **Settings -> Secrets and variables -> Actions -> Repository secrets**，添加以下变量供自动脚本使用：
*   `SERVER_HOST`: 服务器 IP 地址。
*   `SERVER_USER`: 服务器登录用户名 (如 `root`)。
*   `SERVER_PASSWORD`: 服务器登录密码 (或使用 `SERVER_KEY` 配置 SSH 私钥)。

---

## 3. 实现步骤

### 3.1 Dockerfile 改造 (多阶段构建)
编写一个 `Dockerfile`，使其能独立完成所有构建工作。

```dockerfile
# Stage 1: Build Frontend (Node.js)
FROM node:12 as frontend-builder
WORKDIR /app-frontend
# ... copy package.json & install ...
# ... copy source & run build ...

# Stage 2: Build Backend (Maven)
FROM maven:3.8-jdk-8 as backend-builder
WORKDIR /app-backend
# ... copy pom.xml & download deps ...
# ... copy source ...
# 关键：把 Stage 1 编译好的 dist 复制到 SpringBoot 静态资源目录
COPY --from=frontend-builder /app-frontend/dist ./src/main/resources/admin/admin/dist
RUN mvn clean package -DskipTests

# Stage 3: Runtime (JRE)
FROM eclipse-temurin:8-jre-alpine
COPY --from=backend-builder /app-backend/target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 3.2 GitHub Workflow 配置 (.github/workflows/deploy.yml)

主要包含两个 Job：

**Job 1: build_and_deploy**
1.  Checkout 代码。
2.  使用 `docker/build-push-action` 构建镜像。
3.  推送到 `ghcr.io/username/repo:latest`。

**Job 2: deploy_to_server** (依赖 Job 1 成功)
1.  使用 `appleboy/ssh-action` 远程连接服务器。
2.  执行 Shell 脚本：
    ```bash
    cd /opt/project
    docker compose pull     # 拉取最新镜像
    docker compose up -d    # 重启容器
    docker image prune -f   # 清理旧镜像
    ```

---

## 4. 最终效果

1.  **本地开发**: 修改代码 -> `git push`。
2.  **自动构建**: GitHub Action 自动触发，在云端完成编译打包，打出 Docker 镜像。
3.  **自动部署**: 几分钟后，服务器自动拉取新镜像并重启，新功能即时上线。
