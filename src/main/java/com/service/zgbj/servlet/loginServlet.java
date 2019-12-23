package com.service.zgbj.servlet;

import com.service.zgbj.mysqlTab.controller.UserServiceImpl;
import com.service.zgbj.utils.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/Lg/user")
public class loginServlet extends HttpServlet{

    @Autowired
    private UserServiceImpl service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String mobile = req.getParameter("mobile");
        String pwd = req.getParameter("password");
        resp.getWriter().write(service.Login(mobile, pwd));
    }

}
