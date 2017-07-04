# springboot-dubbo-demo

此项目是在SpringBoot中整合使用Dubbo，以达到分布式服务项目。Dubbo是Alibaba开源的分布式服务框架，具体不再介绍。本文是对此项目的简单说明解析。

## 项目结构

![项目结构](http://osjs7p1js.bkt.clouddn.com/post_img/springboot_dubbo/project_structure.png)

分为三个项目，将service接口、service实现、web层分开。

- **springboot-dubbo-api**

![api](http://osjs7p1js.bkt.clouddn.com/post_img/springboot_dubbo/project_structure_api.png)

此项目为service接口层，主要放service接口、实体类、dto、枚举类、异常类等通用类。此项目将打包成jar包在下面两个项目中引用，当然这些是交给maven来处理。

- **springboot-dubbo-service**

![service](http://osjs7p1js.bkt.clouddn.com/post_img/springboot_dubbo/project_structure_service.png)

此项目为具体的业务实现，包括具体的业务代码、Mybatis的mapper接口和sql语句。此项目为服务提供者，service会注册为dubbo服务，使用zookeeper进行管理。

- **springboot-dubbo-web**

![web](http://osjs7p1js.bkt.clouddn.com/post_img/springboot_dubbo/project_structure_web.png)

此项目和前端打交道，为前端输出数据（单纯考虑rest模式下）。服务的消费者，需要注册为服务消费者，从zookeeper中获取已注册的服务。


## 具体实现

- **springboot-dubbo-api**

主要是service接口和实体对象，其中实体对象一定要实现Serializable接口，不然在注册dubbo服务时会报错。

其次在pom.xml文件中，需要将此项目打包为jar文件，具体配置如下

	<?xml version="1.0" encoding="UTF-8"?>
	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	
	  <modelVersion>4.0.0</modelVersion>
	
	  <name>springboot-dubbo-api</name>
	  <groupId>llf</groupId>
	  <artifactId>springboot-dubbo-api</artifactId>
	  <version>1.0-SNAPSHOT</version>
	  <packaging>jar</packaging>
	</project>

在开发过程中，此项目代码有修改需要更新在maven仓库中，具体操作就是`maven install`

- **springboot-dubbo-service**

首先maven依赖，需要注意的时，要将springboot-dubbo-api项目依赖加进来。不然无法实现service接口和使用实体类。

	<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Boot Dubbo 依赖 -->
    <dependency>
      <groupId>io.dubbo.springboot</groupId>
      <artifactId>spring-boot-starter-dubbo</artifactId>
      <version>1.0.0</version>
    </dependency>

    <!--Mybatis-SpringBoot集成-->
    <dependency>
      <groupId>org.mybatis.spring.boot</groupId>
      <artifactId>mybatis-spring-boot-starter</artifactId>
      <version>1.0.0</version>
    </dependency>

    <!-- mysql-connector-java -->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.38</version>
    </dependency>

    <!--服务接口-->
    <dependency>
      <groupId>llf</groupId>
      <artifactId>springboot-dubbo-api</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>

dubbo配置，由于加入的依赖是spring-boot-starter-dubbo，已经和springboot集成了，所以只需要在application.properties(或者appl.yml)文件中加入配置，如下：

	## Dubbo 服务提供者配置
	spring.dubbo.application.name=provider
	spring.dubbo.registry.address=zookeeper://127.0.0.1:2181
	spring.dubbo.protocol.name=dubbo
	spring.dubbo.protocol.port=20880
	spring.dubbo.scan=com.llf.springboot.dubbo

在上面配置中，zookeeper为本地启动的，需要事先安装启动，具体流程不再讲解。scan的包为接口实现的所在的包目录。

**注意：**在service实现类中加入是@Service注解要是dubbo的Service注解

	com.alibaba.dubbo.config.annotation.Service;

### 补充

如果不想使用spring-boot-starter-dubbo依赖，想使用官方的原生依赖，可使用xml配置dubbo。

	<?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
		xsi:schemaLocation="http://www.springframework.org/schema/beans  
	            http://www.springframework.org/schema/beans/spring-beans.xsd  
	            http://code.alibabatech.com/schema/dubbo  
	            http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
	
		<!-- 提供方应用信息，用于计算依赖关系 -->
		<dubbo:application name="llf-provider" />
	
		<!-- 使用zookeeper注册中心暴露服务地址 -->
		<dubbo:registry protocol="zookeeper" address="zookeeper://127.0.0.1:2181" />
	
		<!-- 用dubbo协议在20900端口暴露服务, payload:请求及响应数据包的大小,默认为88388608,现设置为默认值的两倍,为支持大数据量的excel导出 -->
		<dubbo:protocol name="dubbo" port="20918" payload="176477216" />
	
		<!-- 当ProtocolConfig和ServiceConfig某属性没有配置时,采用此缺省值 -->
		<dubbo:provider timeout="30000" threadpool="fixed" threads="500" accepts="1000" />
		
	
		<!-- 资源服务 -->
		<dubbo:service retries="0" interface="com.llf.zookeeper.dubbo.service.UserService" ref="userService" />
	
	</beans>

然后在启动类Application.java中加入配置

	@ImportResource("classpath:provider.xml")

- **springboot-dubbo-web**

此项目maven依赖和service项目一致

dubbo服务配置，项目要对service服务进行消费，需要进行消费者配置。

	# Dubbo 服务消费者配置
	spring.dubbo.application.name=consumer
	spring.dubbo.registry.address=zookeeper://127.0.0.1:2181
	spring.dubbo.scan=com.llf.springboot.dubbo


**注意：**在controller进行service注入时需要使用@Reference

	com.alibaba.dubbo.config.annotation.Reference


## 使用

1. 首先确定springboot-dubbo-api项目install到maven仓库

2. 启动本地zookeeper

3. 先启动springboot-dubbo-service项目，再启动springboot-dubbo-web

4. 访问web资源地址，获取数据


## 项目源代码

[springboot-dubbo-demo](https://github.com/phycholee/springboot-dubbo-demo)