个人电影收藏管理系统
================

项目介绍
--------
本系统为个人电影收藏管理工具，面向电影爱好者提供电影收藏管理、个人评分、私人影评、公开评价、社区评分查看等功能。系统采用 B/S 架构，支持多用户，个人收藏数据相互隔离，同时集成 TMDB API 获取电影信息。

本项目为华南农业大学数学与信息学院《数据库系统课程设计》项目，代号：银幕记忆。


技术栈
------
后端：Java Servlet（JDK 21）
前端：HTML + CSS + JavaScript
数据库：MySQL 5.7 / 8.0
构建工具：Maven
服务器：Tomcat 10.1.x
版本控制：Git


项目结构
--------
movie-collection/
├── src/main/
│   ├── java/               # Java 源代码（Servlet、DAO、Bean、工具类）
│   ├── resources/          # 配置文件（db.properties 等）
│   └── webapp/             # 前端页面（HTML/CSS/JS）及 WEB-INF
├── pom.xml                 # Maven 依赖
├── .gitignore
└── README.md


开发环境
--------
- JDK 21
- Tomcat 10.1.x
- MySQL 5.7 / 8.0
- IntelliJ IDEA（推荐）


运行说明
--------
1. 克隆项目到本地
   git clone <仓库地址>

2. 使用 IDEA 打开项目，配置 JDK 21 + Tomcat 10.1.x

3. 创建 MySQL 数据库，执行项目中的 SQL 建表脚本

4. 修改 src/main/resources/db.properties 中的数据库连接信息

5. 配置 Tomcat 运行配置，部署当前项目

6. 启动项目，访问：http://localhost:8080/movie-collection


团队协作规范（每日工作流程）
============================

本项目由 3 人协作开发：李晨睿、马绵聪、梁欣楠
主分支为 main，所有功能开发在独立分支上进行，完成后合并到 main


【每日工作标准流程】

步骤1：每天开始工作前
git checkout main
git pull
拉取最新代码

步骤2：每天开始工作前
基于最新 main 创建/切换到自己的功能分支
如：git checkout -b feature/xxx

步骤3：开发过程中
在自己的分支上完成功能开发、修复 Bug、编写前端页面或 SQL 调整

步骤4：每天工作结束时
提交当天代码到自己的远程分支，并推送到仓库
git add .
git commit -m "提交说明"
git push origin feature/xxx

步骤5：功能完成后
发起 Pull Request（PR）或合并请求，请求将功能分支合并到 main

步骤6：合并前
确保与 main 无冲突，并完成简单自测


【每天可以提交的内容】

- 新增/修改的 Java Servlet 代码
- 新增/修改的 JSP 或 HTML/CSS/JS 文件
- 数据库建表或修改表的 SQL 脚本
- 配置文件修改（如 db.properties）
- 文档更新（如 README、需求文档的补充说明）


【提交说明格式】

<类型>: <简短描述>

类型示例：
- feat: 新增功能
- fix: 修复 Bug
- docs: 文档更新
- style: 前端样式调整
- refactor: 代码重构
- db: 数据库脚本变更

示例：
feat: 完成按导演搜索电影功能

fix: 修复个人评分显示不正确的Bug


【合并到 main 的要求】

在将任意内容合并到 main 分支之前，必须满足以下条件：

1. 该功能/修改已在自己的分支上完整实现并通过基本测试
2. 与当前 main 分支无代码冲突（如有冲突需先解决）
3. 不破坏现有核心功能（增删改查、查询、评分展示等）
4. 涉及数据库变更时，必须同时提供对应的 SQL 脚本
5. 提交信息清晰，说明本次变更内容


【分支命名建议】

新功能     ：feature/movie-search

Bug 修复   ：fix/rating-display

前端调整   ：ui/movie-card-style

数据库     ：db/add-movie-public-fields


【冲突处理原则】

- 若 git pull 或合并时出现冲突，由涉及冲突的成员共同协商解决
- 禁止强制推送（--force）到 main 分支
- 解决冲突后，重新测试受影响的功能模块


主要功能模块（对应需求文档）
============================

| 模块         | 功能说明                                    |
| ------------ | ------------------------------------------- |
| 电影收藏管理 | 添加、编辑、删除、查看个人电影收藏          |
| 电影查询     | 按名称/导演/演员/评分/地区/类型筛选         |
| 社区互动     | 发布公开评价、查看他人评价、查看综合评分    |


备注
----
- 本项目为课程设计项目，无商业用途
- 外部 API：TMDB（仅用于获取电影信息）
- 项目周期：2026年3月 — 2026年6月