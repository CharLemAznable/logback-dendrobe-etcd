### logback-dendrobe-etcd

[![Build](https://github.com/CharLemAznable/logback-dendrobe-etcd/actions/workflows/build.yml/badge.svg)](https://github.com/CharLemAznable/logback-dendrobe-etcd/actions/workflows/build.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.charlemaznable/logback-dendrobe-etcd/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.charlemaznable/logback-dendrobe-etcd/)
[![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)
![GitHub code size](https://img.shields.io/github/languages/code-size/CharLemAznable/logback-dendrobe-etcd)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-dendrobe-etcd&metric=alert_status)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-dendrobe-etcd)

[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-dendrobe-etcd&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-dendrobe-etcd)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-dendrobe-etcd&metric=bugs)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-dendrobe-etcd)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-dendrobe-etcd&metric=security_rating)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-dendrobe-etcd)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-dendrobe-etcd&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-dendrobe-etcd)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-dendrobe-etcd&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-dendrobe-etcd)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-dendrobe-etcd&metric=sqale_index)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-dendrobe-etcd)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-dendrobe-etcd&metric=code_smells)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-dendrobe-etcd)

[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-dendrobe-etcd&metric=ncloc)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-dendrobe-etcd)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-dendrobe-etcd&metric=coverage)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-dendrobe-etcd)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-dendrobe-etcd&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-dendrobe-etcd)

使用Etcd热更新[logback-dendrobe](https://github.com/CharLemAznable/logback-dendrobe)配置.

##### Maven Dependency

```xml
<dependency>
  <groupId>com.github.charlemaznable</groupId>
  <artifactId>logback-dendrobe-etcd</artifactId>
  <version>2023.2.2</version>
</dependency>
```

##### Maven Dependency SNAPSHOT

```xml
<dependency>
  <groupId>com.github.charlemaznable</groupId>
  <artifactId>logback-dendrobe-etcd</artifactId>
  <version>2024.0.0-SNAPSHOT</version>
</dependency>
```

#### 本地配置需要读取的Etcd配置坐标

在本地类路径默认配置```logback-dendrobe.properties```文件中, 添加如下配置:

```
logback.etcd.namespace=XXX
logback.etcd.key=YYY
```

即指定使用Etcd配置```namespace:XXX key:YYY```热更新logback-dendrobe配置.

```logback.etcd.namespace```配置默认值: Logback
```logback.etcd.key```配置默认值: default

#### 使用Etcd配置logback-dendrobe数据库日志的Eql连接

当配置数据库日志为```{logger-name}[eql.connection]=XXX```时, 读取Etcd配置```namespace:EqlConfig key:XXX```作为Eql连接配置.

#### 使用Etcd配置logback-dendrobe Vert.x日志的Vert.x实例

当配置Vert.x日志为```{logger-name}[vertx.name]=XXX```时, 读取Etcd配置```namespace:VertxOptions key:XXX```作为Vert.x实例配置.

#### 使用Etcd配置logback-dendrobe ElasticSearch日志的es客户端

当配置ElasticSearch日志为```{logger-name}[es.name]=XXX```时, 读取Etcd配置```namespace:EsConfig key:XXX```作为es客户端配置.
