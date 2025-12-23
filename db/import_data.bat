@echo off
setlocal enabledelayedexpansion

:: 设置MySQL连接参数
set "MYSQL_HOST=localhost"
set "MYSQL_PORT=3306"
set "MYSQL_USER=root"
set "MYSQL_PASS=123456"
set "CURRENT_DIR=%cd%"
:: 初始化变量
set "SQL_FILE="
set "DB_NAME="
set "FILE_COUNT=0"

:: 显示当前目录
echo 当前目录：%cd%

:: 查找当前目录下的.sql文件
echo 正在搜索.sql文件...
for %%f in (%~dp0*.sql) do (
    set /a FILE_COUNT+=1
    set "SQL_FILE=%%f"
    set "DB_NAME=%%~nf"
    echo 发现文件：%%f
)

:: 检查文件数量
if %FILE_COUNT% equ 0 (
    echo 错误：当前目录下没有找到任何.sql文件！
    pause
    exit /b 1
)

if %FILE_COUNT% gtr 1 (
    echo 警告：当前目录下有多个.sql文件，将使用第一个文件：%SQL_FILE%
)

echo 使用SQL文件：%SQL_FILE%
echo 数据库名称变量设置为：DB_NAME=%DB_NAME%

:: 检查MySQL命令是否可用
where mysql >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo 错误：未找到mysql命令，请确保MySQL客户端已安装并添加到PATH环境变量！
    pause
    exit /b 1
)

:: 执行MySQL导入命令
echo 正在导入到MySQL数据库...
echo 请稍候...
:: 检查数据库是否存在，如果存在则删除
mysql -u%MYSQL_USER% -p%MYSQL_PASS% -P%MYSQL_PORT% -e "DROP DATABASE IF EXISTS %DB_NAME%;"

:: 创建数据库
mysql -u%MYSQL_USER% -p%MYSQL_PASS% -P%MYSQL_PORT% -e "CREATE DATABASE %DB_NAME%;"

mysql -u%MYSQL_USER% -p%MYSQL_PASS% -P%MYSQL_PORT% --default-character-set=utf8mb4 %DB_NAME% < "%SQL_FILE%"

:: 检查执行结果
if %ERRORLEVEL% equ 0 (
    echo 导入成功！
    echo 数据库 %DB_NAME% 已从文件 %SQL_FILE% 导入
) else (
    echo 导入失败！错误代码：%ERRORLEVEL%
    echo 可能的原因：
    echo 1. MySQL服务未运行
    echo 2. 用户名或密码错误
    echo 3. 数据库不存在（需要先创建数据库）
    echo 4. SQL文件语法错误
    echo 5. 网络连接问题
)

pause