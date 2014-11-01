package com.controller;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.action.BaseAction;
import com.model.AuthCodeModel;
import com.util.WebUtil;

@Controller
@RequestMapping("authCode")
public class AuthCodeController extends BaseAction {

    @RequestMapping
    public void index(HttpServletRequest request, HttpServletResponse response, String... args) throws IOException, ServletException {
        // 获取随机码和图片
        AuthCodeModel authCodeModel = new AuthCodeModel();
        // 把随机码写入到session中
        WebUtil.setAuthCode(request, authCodeModel.getString());

        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos = response.getOutputStream();
        try {
            ImageIO.write(authCodeModel.getImage(), "jpeg", sos);
        } finally {
            sos.close();
        }
    }
}
