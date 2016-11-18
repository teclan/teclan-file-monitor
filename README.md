## 项目介绍

监控某个目录下的文件状态，如果发现文件被修改，从备份目录中恢复对应文件。
程序开始，会先进入备份目录下采集文件状态，包括文件大小，文件最后修改时间
以及 SHA-256 存入文件数据库，数据库相关配置请参考配置文件。

## 环境

1、JDK
2、Maven

## 依赖项目

[teclan-guice](https://github.com/teclan/teclan-guice)


[teclan-utils](https://github.com/teclan/teclan-utils)

## 配置

监控目录和备份目录用户在配置文件 config.conf中指定
```
config {

  file {
  	  # 程序监控目录，内容与备份目录一致
	  monitor-dir = "/home/teclan/tmp/teclan-monitor"
	  # 文件备份目录，当文件发生改变时，从此目录中恢复
	  backup-dir = "/home/teclan/tmp/teclan"
	  }
	  
	  
  thread {
    # 文件恢复的最大并发数
    max = 3
  }

  ## 数据库配置项
  db {
    ## 数据库连接名称
    name = "default"
    ## 数据库迁移配置项
    migration {
      ## 应用启动时是否执行迁移
      migrate = true
    }
    ## JDBC连接配置项
    jdbc {
      ## 连接驱动
      driver   = "org.h2.Driver"
      ## 数据库url
      url-template:"jdbc:h2:file:%s"
      ## 数据库文件相对应用的存放路径
      db-path      = "db/teclan-monitor"
      ## 用户名
      user     = "system"
      ## 密码
      password = "65536"
    }
  }
}

```
## 打包

在项目目录下执行以下命令，在 target 目录下生成 teclan-monitor-0.0.1-SNAPSHOT-bin.zip
```
mvn package -Dmaven.test.skip
```
## 运行
解压 teclan-monitor-0.0.1-SNAPSHOT-bin.zip ，执行 bin 目录下的 startup 文件即可



