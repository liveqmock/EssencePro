package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.model.User;
import com.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(params = "action=reg")
    public ModelAndView reg(User user) throws Exception {
        System.out.println("注册时间被拦截");
        userService.insert(user);
        return new ModelAndView("profile", "user", user);
    }

    @RequestMapping("get")
    public ModelAndView get() {
        System.out.println("2222222222");
        User user = userService.getUserMapper().selectByPrimaryKey(1);
        System.out.println(JSON.toJSONString(user));
        return new ModelAndView("");
    }
    // other option ...
}
