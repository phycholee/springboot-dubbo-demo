package com.llf.springboot.dubbo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by PhychoLee on 2017/2/24.
 */
@RestController
@RequestMapping("")
public class HelloController {

    @GetMapping("service")
    public String hello(){
        return "Hello Service";
    }
}
