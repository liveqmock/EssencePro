package com.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.model.User;
import com.service.UserService;

@Controller
@RequestMapping("login")
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "login_bak")
    public void login_bak(HttpServletResponse res) throws IOException {

        User user = new User();
        user.setBm("001");
        user.setName("黄泉颤抖");
        int bool = userService.insert(user);
        if (bool > 0) {
            // 解决乱码
            res.setContentType("text/html;charset=UTF-8");
            res.getWriter().print("插入成功！");
        } else {
            res.setContentType("text/html;charset=UTF-8");
            res.getWriter().print("插入失败！");
        }
    }

    @RequestMapping("xxx")
    public ModelAndView login(HttpServletRequest req, HttpServletResponse res) throws IOException {
        System.out.println("1111111111111111111");
        return new ModelAndView("view/login");

    }

}
