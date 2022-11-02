# taskmanagement

#### 1.项目介绍

软件后端采用 **springboot+mybatis plus+redis+springsecurity+jwt**编写，前端使用vue编写

支持签到、任务发布、讨论等功能，功能持续开发

#### 2.安装方法

**注意**：可以在application.yml之中切换环境，更改配置

1.**普通安装**

下载jar包 使用 java -jar运行即可

2.**docker安装**

1. **拉取镜像**docker pull registry.cn-beijing.aliyuncs.com/huawei_club/taskmanagement:0.1
2. **运行镜像** docker run -d -p 8081:8081 --name [容器名字]  --net [你的网络]  taskmanagement:[TAG]

3.**idea运行**

运行TaskmanagementApplication即可

#### 3.成员贡献

# taskmanagement-main
