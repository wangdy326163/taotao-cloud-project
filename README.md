# taotao-cloud-project

<p align="center">
  <img src='https://img.shields.io/badge/license-Apache%202-green' alt='License'/>
  <img src="https://img.shields.io/badge/Spring-5.3.4-red" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-2.4.3-orange" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud-2020.0.1-yellowgreen" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud%20alibaba-2.2.5.RELEASE-blue" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Elasticsearch-6.8.7-green" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Mybatis%20Plus-3.4.2-yellow" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Knife4j-3.0.2-brightgreen" alt="Downloads"/>
</p>

## 1. 如果您觉得有帮助，请点右上角 "Star" 支持一下谢谢

TaoTaoCloud是一款基于Spring Cloud Alibaba的微服务架构。旨在为大家提供技术框架的基础能力的封装，减少开发工作，让您只关注业务。

## 2. 总体架构图
![mark](./snapshot/springcloud微服务架构图.jpeg)


## 3. 核心依赖 
依赖 | 版本
---|---
Spring |  5.3.4 
Spring Boot |  2.4.3  
Spring Cloud | 2020.0.1 
Spring Cloud alibaba | 2.2.5.RELEASE  
Spring Security OAuth2 | 5.4.5
Mybatis Plus | 3.4.2
Hutool | 5.5.9
Lombok | 1.18.18
Mysql | 8.0.20
Querydsl | 4.4.0
Swagger | 3.3.0
Knife4j | 3.0.2
Redisson | 3.15.0
Lettuce | 6.0.2.RELEASE
Elasticsearch | 6.8.7
Xxl-job | 2.2.0
EasyCaptcha | 1.6.2

## 4. 演示地址
  * TaoTao Cloud 首页: [https://taotaocloud.top](https://taotaocloud.top)
  * 博客系统地址: [https://blog.taotaocloud.top](https://blog.taotaocloud.top)
  * 大屏监控系统地址: [https://datav.taotaocloud.top](https://datav.taotaocloud.top)
  * 后台管理地址: [https://backend.taotaocloud.top](https://backend.taotaocloud.top)
  * 账号密码：admin/admin
    
  * 监控账号密码：admin/admin
  * Grafana账号：admin/admin
  * 任务管理账号密码：admin/admin
  * Sentinel：sentinel/sentinel

## 5. 功能特点

* 前后端分离的企业级微服务架构
* 主体框架：采用最新的Spring Cloud 2020.0.1, Spring Boot 2.4.3, Spring Cloud Alibaba 2.2.5.RELEASE版本进行系统设计；
* 统一注册：支持Nacos作为注册中心，实现多配置、分群组、分命名空间、多业务模块的注册和发现功能；
* 统一认证：统一Oauth2认证协议，采用jwt的方式，实现统一认证，并支持自定义grant_type实现手机号码登录，第三方登录正在开发中；
* 业务监控：利用Spring Boot Admin 来监控各个独立Service的运行状态；利用Hystrix Dashboard来实时查看接口的运行状态和调用频率等。
* 内部调用：集成了Feign和Dubbo两种模式支持内部调用，并且可以实现无缝切换，适合新老程序员，快速熟悉项目；
* 业务熔断：采用Sentinel实现业务熔断处理，避免服务之间出现雪崩;
* 身份注入：通过注解的方式，实现用户登录信息的快速注入；
* 在线文档：通过接入Knife4j，实现在线API文档的查看与调试;
* 代码生成：基于Mybatis-plus-generator自动生成代码，提升开发效率，生成模式不断优化中，暂不支持前端代码生成；
* 消息中心：集成消息中间件RocketMQ，对业务进行异步处理;
* 业务分离：采用前后端分离的框架设计，前端采用vue-element-admin
* 链路追踪：自定义traceId的方式，实现简单的链路追踪功能
* 多租户功能：集成Mybatis Plus,实现saas多租户功能  
* 基于 Spring Cloud Hoxton 、Spring Boot 2.4、 OAuth2 的RBAC权限管理系统  
* 基于数据驱动视图的理念封装 react , antd  
* 提供对常见容器化支持 Docker、Kubernetes、Rancher2 支持  
* 提供 lambda 、stream api 、webflux 的生产实践   
* 提供应用管理，方便第三方系统接入，**支持多租户(应用隔离)**
* 引入组件化的思想实现高内聚低耦合并且高度可配置化
* 注重代码规范，严格控制包依赖，每个工程基本都是最小依赖

## 6. 模块说明
```
taotao-cloud-project -- 父项目
│  ├─taotao-cloud-bigdata -- 大数据模块
│  │  ├─taotao-cloud-bigdata-azkaban -- azkaban模块
│  │  ├─taotao-cloud-bigdata-clickhouse -- clickhouse模块
│  │  ├─taotao-cloud-bigdata-datax -- datax模块
│  │  ├─taotao-cloud-bigdata-elasticsearch -- elasticsearch模块
│  │  ├─taotao-cloud-bigdata-flink -- flink模块
│  │  ├─taotao-cloud-bigdata-flum -- flum模块
│  │  ├─taotao-cloud-bigdata-hadoop -- hadoop模块
│  │  ├─taotao-cloud-bigdata-hbase -- hbase模块
│  │  ├─taotao-cloud-bigdata-hive -- hive模块
│  │  ├─taotao-cloud-bigdata-hudi -- hudi模块
│  │  ├─taotao-cloud-bigdata-impala -- impala模块
│  │  ├─taotao-cloud-bigdata-kafka -- kafka模块
│  │  ├─taotao-cloud-bigdata-kudu -- kudu模块
│  │  ├─taotao-cloud-bigdata-openresty -- openresty模块
│  │  ├─taotao-cloud-bigdata-phoenix -- phoenix模块
│  │  ├─taotao-cloud-bigdata-spark -- spark模块
│  │  ├─taotao-cloud-bigdata-storm -- storm模块
│  │  ├─taotao-cloud-bigdata-tez -- tez模块
│  │  ├─taotao-cloud-bigdata-trino -- trino模块
│  │  ├─taotao-cloud-bigdata-zookeeper -- zookeeper模块
│  ├─taotao-cloud-config -- 全局软件配置文件模块
│  ├─taotao-cloud-container -- 容器模块
│  │  ├─taotao-cloud-docker -- docker模块
│  │  ├─taotao-cloud-kubernetes -- kubernetes模块
│  ├─taotao-cloud-demo -- demo模块
│  │  ├─taotao-cloud-demo-kafka -- kafka模块
│  │  ├─taotao-cloud-demo-mqtt -- mqtt模块
│  │  ├─taotao-cloud-demo-rocketmq -- rocketmq模块
│  │  ├─taotao-cloud-demo-seata -- seata模块
│  │  ├─taotao-cloud-demo-sharding-jdbc -- sharding-jdbc模块
│  │  ├─taotao-cloud-demo-sso -- sso模块
│  │  ├─taotao-cloud-demo-transaction -- transaction模块
│  │  ├─taotao-cloud-demo-youzan -- youzan模块
│  ├─taotao-cloud-dependencies -- 全局公共依赖模块
│  ├─taotao-cloud-java -- java模块
│  │  ├─taotao-cloud-concurrent  -- concurrent模块
│  │  ├─taotao-cloud-javaee -- javaee模块
│  │  ├─taotao-cloud-javase -- javase模块
│  │  ├─taotao-cloud-javaweb -- javaweb模块
│  ├─taotao-cloud-microservice -- 微服务模块
│  │  ├─taotao-cloud-aftersale  -- aftersale模块
│  │  ├─taotao-cloud-bulletin  -- bulletin模块
│  │  ├─taotao-cloud-cart  -- cart模块
│  │  ├─taotao-cloud-coupon  -- coupon模块
│  │  ├─taotao-cloud-customer  -- customer模块
│  │  ├─taotao-cloud-dfs  -- dfs模块
│  │  ├─taotao-cloud-backend  -- backend模块
│  │  ├─taotao-cloud-front  -- front模块
│  │  ├─taotao-cloud-log -- log模块
│  │  ├─taotao-cloud-logistics  -- logistics模块
│  │  ├─taotao-cloud-mail  -- mail模块
│  │  ├─taotao-cloud-member  -- member模块
│  │  ├─taotao-cloud-news  -- news模块
│  │  ├─taotao-cloud-oauth2  -- oauth2模块
│  │  ├─taotao-cloud-operation  -- operation模块
│  │  ├─taotao-cloud-order  -- order模块
│  │  ├─taotao-cloud-pay -- pay模块
│  │  ├─taotao-cloud-product  -- product模块
│  │  ├─taotao-cloud-recommend  -- recommend模块
│  │  ├─taotao-cloud-report  -- report模块
│  │  ├─taotao-cloud-search  -- search模块
│  │  ├─taotao-cloud-seckill   -- seckill模块
│  │  ├─taotao-cloud-settlement  -- settlement模块
│  │  ├─taotao-cloud-sms  -- sms模块
│  │  ├─taotao-cloud-stock  -- stock模块
│  │  ├─taotao-cloud-uc  -- uc模块
│  │  ├─taotao-cloud-starter  -- starter模块
│  ├─taotao-cloud-netty -- netty模块
│  ├─taotao-cloud-offline -- 离线数据分析模块
│  │  ├─taotao-cloud-offline-warehouse  -- 数据仓库模块
│  │  ├─taotao-cloud-offline-weblog -- web日志分析模块
│  ├─taotao-cloud-opencv -- opencv模块
│  ├─taotao-cloud-plugin -- 插件模块
│  │  ├─taotao-cloud-gradle-plugin  -- gradle-plugin模块
│  │  ├─taotao-cloud-idea-plugin -- idea-plugin模块
│  ├─taotao-cloud-processor -- java processor模块
│  ├─taotao-cloud-python -- python模块
│  │  ├─taotao-cloud-django  -- django模块
│  │  ├─taotao-cloud-oldboy  -- oldboy模块
│  │  ├─taotao-cloud-scrapy  -- scrapy模块
│  │  ├─taotao-cloud-spider  -- spider模块
│  │  ├─taotao-cloud-tornado  -- tornado模块
│  ├─taotao-cloud-reactive -- spring web响应式模块
│  ├─taotao-cloud-realtime -- 实时数据分析模块
│  │  ├─taotao-cloud-realtime-datalake  -- 数据湖模块
│  │  ├─taotao-cloud-realtime-mall -- 商城日志分析模块
│  │  ├─taotao-cloud-realtime-recommend -- 实时推荐模块
│  │  ├─taotao-cloud-realtime-travel -- 实时旅游模块
│  ├─taotao-cloud-rpc -- rpc模块
│  ├─taotao-cloud-scala -- scala模块
│  ├─taotao-cloud-spring-native -- spring native模块
│  ├─taotao-cloud-spring-source -- spring 源码模块
│  ├─taotao-cloud-spring-standlone -- 单项目模块
│  ├─taotao-cloud-web -- 前端模块
│  │  ├─taotao-cloud-backend  -- 后台管理模块
│  │  ├─taotao-cloud-datav -- 大屏模块
│  │  ├─taotao-cloud-electron -- pc端app模块
│  │  ├─taotao-cloud-front -- 商城前端模块
│  │  ├─taotao-cloud-mall  -- 商城小程序模块
│  │  ├─taotao-cloud-music -- 音乐小程序模块
├─────│
```

## 7.开源共建

1. 欢迎提交 [pull request](https://github.com/shuigedeng/taotao-cloud-project)，注意对应提交对应 `dev` 分支

2. 欢迎提交 [issue](https://github.com/shuigedeng/taotao-cloud-project/issues)，请写清楚遇到问题的原因、开发环境、复显步骤。

3. 不接受`功能请求`的 [issue](https://github.com/shuigedeng/taotao-cloud-project/issues)，功能请求可能会被直接关闭。  

4. mail: <a href="981376577@qq.com">981376577@qq.com</a> | <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3130998334&site=qq&menu=yes"> QQ: 981376577</a>    

## 8.项目截图
