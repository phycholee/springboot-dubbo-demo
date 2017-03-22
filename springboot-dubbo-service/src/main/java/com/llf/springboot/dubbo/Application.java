package com.llf.springboot.dubbo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by PhychoLee on 2016/10/13.
 */
@SpringBootApplication
//@ImportResource("classpath:provider.xml")
@MapperScan("com.llf.zookeeper.dubbo.mapper")
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
