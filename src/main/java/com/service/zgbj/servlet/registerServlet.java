package com.service.zgbj.servlet;

import com.service.zgbj.mysqlTab.controller.HistoryServiceImpl;
import com.service.zgbj.mysqlTab.controller.UserServiceImpl;
import com.service.zgbj.utils.OfTenUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/rgs/user")
public class registerServlet extends HttpServlet{

    @Autowired
    private UserServiceImpl service;
    @Autowired
    private HistoryServiceImpl historyService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        String email = req.getParameter("email");
        String imageUrl = req.getParameter("imageUrl");
        String location = req.getParameter("location");
        String mobile = req.getParameter("mobile");
        String pwd = req.getParameter("password");
        String sex = req.getParameter("sex");
        String name = req.getParameter("name");
        String randomUid = OfTenUtils.getRandomUid();
        historyService.createTable(randomUid);
        service.createTableFriend(randomUid);
        service.createTableFriendMsg(randomUid);
        resp.getWriter().write(service.register(email,imageUrl,location,mobile,pwd,sex,name,randomUid));
    }
}
