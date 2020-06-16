package com.service.zgbj.servlet;

import com.service.zgbj.mysqlTab.controller.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/friend/info")
public class friendInfoServlet extends HttpServlet{
    @Autowired
    private UserServiceImpl service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String uid = req.getParameter("uid");
        resp.getWriter().write(service.getFriendUserInfo(id,uid));
    }
}
