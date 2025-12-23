# 项目更新与重启指南 (Update & Deploy Guide)

由于本项目采用了 **物理耦合（前端资源打包进后端 Jar）** 的架构，任何代码修改（无论是 Java 还是 Vue/HTML）都需要按照严格的顺序重新构建才能生效。

以下是修改代码后，更新 Docker 环境的标准步骤。

## 📋 核心流程概览

1.  **前端构建** (`npm run build`)：将 Vue 源码编译成静态文件 (HTML/JS/CSS)。
2.  **后端打包** (`mvn package`)：将编译好的前端资源 + Java 源码打包成最终的 Jar 包。
3.  **容器重启** (`docker-compose up`)：基于新的 Jar 包重新构建镜像并启动容器。

---

## 🛠 详细操作步骤 (Windows PowerShell)

请确保您处于项目的根目录下 (例如 `E:\springbootpx13e`)。

### 第一步：编译后台前端 (仅修改 admin 代码时需要)
如果您只修改了 Java 代码或前台(`front`)的 HTML，可跳过此步。如果您修改了后台管理(`admin`)的代码，必须执行：

```powershell
# 进入后台前端目录
cd src/main/resources/admin/admin

# 编译生成dist目录 (需要确保已安装 Node 12 环境)
npm run build

# 返回项目根目录
cd ../../../..
```

### 第二步：打包后端应用
这一步会将最新的 Java 代码以及刚才编译好的前端资源一起打入 Jar 包。

```powershell
# 清理旧target并打包 (跳过测试以加快速度)
mvn clean package -DskipTests
```
*成功后，您应该能在 `target` 目录下看到一个新的 `.jar` 文件。*

### 第三步：重新构建并启动容器
这一步会触发 Dockerfile 重新执行，把新的 Jar 包 `COPY` 进去。

```powershell
# 停止当前容器（如果正在运行）
docker-compose down

# 重新构建镜像并后台启动
# --build 参数强制重新执行 Dockerfile 构建
docker-compose up -d --build
```

---

## ⚡ 一键更新脚本 (PowerShell)

为了方便，您可以直接复制以下命令在 PowerShell 中一次性执行所有操作：

```powershell
# 1. 编译前端
cd src/main/resources/admin/admin; npm run build; cd ../../../..;

# 2. 打包后端
mvn clean package -DskipTests;

# 3. 重启 Docker
docker-compose down; docker-compose up -d --build;
```

## ❓ 常见问题

**Q: 我只改了前台 (front) 的 HTML 文件，需要 npm run build 吗？**
A: 不需要。`front` 目录是纯静态的，直接进行第二步 `mvn package` 打包即可。

**Q: 我改了 Java 代码，必须重启 Docker 吗？**
A: 是的。Java 是编译型语言，必须重新打 Jar 包并替换进容器才能生效。

**Q: 数据库里的数据会丢吗？**
A: 不会。我们在 `docker-compose.yml` 中配置了 `volumes` 挂载，数据保存在本地的 `db_data` 目录中，重启容器不会丢失数据。
