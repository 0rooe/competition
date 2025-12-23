# 学科竞赛管理系统 (Competition Management System)

本项目是一个基于 Spring Boot + Vue + LayUI 的学科竞赛管理系统，包含后台管理系统和前台门户网站。系统支持学生报名参赛、教师管理赛项、新闻资讯发布以及奖项统计等功能。

## 🛠 技术栈

### 后端
- **核心框架**: Spring Boot 2.2.2.RELEASE
- **ORM框架**: MyBatis + MyBatis-Plus 2.3
- **权限安全**: Apache Shiro 1.3.2
- **数据库**: MySQL 5.7+
- **工具库**: Hutool, FastJson, Baidu AI SDK

### 前端
- **后台管理**: Vue.js + Element UI (位于 `src/main/resources/admin/admin`)
- **前台门户**: HTML + LayUI + Vue.js (CDN引用) (位于 `src/main/resources/front/front`)

## 📂 项目结构

```
springbootpx13e/
├── src/main/java/com/          # 后端 Java 源码
│   ├── config/                 # 配置类 (WebMvc, MybatisPlus等)
│   ├── controller/             # 控制层接口
│   ├── entity/                 # 实体类
│   ├── service/                # 业务逻辑层
│   └── dao/                    # 数据访问层
├── src/main/resources/         # 资源文件
│   ├── application.yml         # 核心配置文件
│   ├── mapper/                 # MyBatis XML 映射文件
│   ├── admin/admin/            # 后台管理系统源码 (Vue CLI项目)
│   └── front/front/            # 前台门户静态页面
└── pom.xml                     # Maven 依赖配置
```

## ✨ 功能模块

系统的主要功能分为前台和后台两个部分，涵盖三种角色：**管理员**、**教师**、**学生**。

### 1. 前台门户 (Public Portal)
- **首页**: 系统概况与导航。
- **新闻资讯**: 查看最新的比赛通知和公告。
- **赛项信息**: 浏览各类学科竞赛的详细介绍。
- **赛项报名**: 学生在线报名参加比赛。
- **奖项统计**: 公示获奖名单及统计信息。
- **个人中心**: 学生/教师查看个人信息、报名状态等。

### 2. 后台管理 (Admin Dashboard)
- **用户管理**:
    - **学生管理**: 管理学生基本信息。
    - **教师管理**: 管理教师信息及权限。
    - **管理员管理**: 系统管理员账号维护。
- **竞赛业务**:
    - **赛项信息管理**: 发布和维护竞赛项目。
    - **报名信息管理**: 审核和管理学生的报名记录。
    - **赛项成绩管理**: 录入和查询比赛成绩。
- **系统资讯**: 轮播图管理、新闻资讯发布。

## 🚀 快速开始

### 环境准备
- JDK 1.8
- MySQL 5.7 或 8.0
- Maven 3.6+
- Node.js 12.x (推荐 12.16.2，用于编译后台前端)

### 安装与运行

1.  **数据库配置**
    - 创建数据库 `springbootpx13e`。
    - 导入数据库脚本（通常在 `db` 目录下或由 JPA/Hibernate 自动生成，本项目使用了 MyBatis-Plus，请检查是否有 `.sql` 文件）。
    - 修改 `src/main/resources/application.yml` 中的数据库连接信息（用户名/密码）。

2.  **后端启动**
    - 使用 IDEA 打开项目。
    - 等待 Maven 依赖下载完成。
    - 运行 `com.SpringbootSchemaApplication` 主类。
    - 服务启动在端口 `8080`。

3.  **前端访问**
    - 本项目采用了静态资源映射，前后端打包在一起。启动后端后，直接访问以下地址：
    - **前台首页**: `http://localhost:8080/springbootpx13e/front/index.html`
    - **后台管理**: `http://localhost:8080/springbootpx13e/admin/index.html` (取决于构建后的路径，开发模式下可独立运行 Node 服务)。

### 前端独立开发 (可选)
如果需要修改后台页面：
```bash
cd src/main/resources/admin/admin
# 切换到 Node 12 环境
nvm use 12.16.2
# 安装依赖 (推荐使用 npm 配合淘宝源)
npm install
# 启动开发服务器
npm run serve
```
