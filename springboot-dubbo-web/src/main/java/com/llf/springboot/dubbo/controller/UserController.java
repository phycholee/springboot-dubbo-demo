package com.llf.springboot.dubbo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.llf.springboot.dubbo.model.User;
import com.llf.springboot.dubbo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by PhychoLee on 2017/2/23.
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Reference(version = "1.0.0")
    private UserService userService;

    @RequestMapping("hello")
    @ResponseBody
    public String hello(){
        User user = userService.sayHi("Oliver");
        return "Hi! I'm " + user.getName() + ", " + user.getAge() + " years old. " + user.getIntroduce();
    }
}
