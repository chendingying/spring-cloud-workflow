## 模块介绍
>  前端工程

| 模块名称      |          备注说明           |
| :---------:   | :-------------------------: |
| work-admin    |          管理台        |
| form-modeler  | 表单模型（设计，明细） |
| flow-modeler  | 流程模型（设计，监控） |

## 后端接口模块

>  模块功能
 
| 模块名称                       |          备注说明           |
| :-------------:    |    :----------------------: |
| common-module      |          公共模块           |
| flow-service       |          流程接口           |
| form-service       |          表单接口           |
| identity-service   |          人员接口           |

>  错误码

| 模块名称              |          备注说明         |
| :----------:          |   :---------------------: |
| 0 开头                |         公共部分错误      |
| 1 开头                |         流程模块错误      |
| 2 开头                |         人员模块错误      |
| 3 开头                |         表单模块错误      |

>  后端技术

| 技术名称              |          备注说明         |
| :----------------:    |   :---------------------: |
| Java v1.8             |         编码语言          |
| Maven                 |         构建工具          |
| SpringBoot            |代码框架（后续springcloud）|
| Flowable JPA Mybatis  |         第三方组件        |
| Mysql                 |          数据库           |

```
错误码：统一使用message配置

日志统一使用logback-spring.xml

```

## Demo 演示
[系统控制台](http://work.plumdo.com) 

[表单设计器](https://wengwh.github.io/plumdo-work)
