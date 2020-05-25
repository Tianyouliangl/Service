package com.service.zgbj.servlet;

import com.service.zgbj.mysqlTab.controller.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fengzhangwei on 2020/5/20.
 */
@WebServlet("/friend/addFriend")
public class addFriendServlet extends HttpServlet {
    @Autowired
    private UserServiceImpl service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from_id = req.getParameter("from_id");
        String to_id = req.getParameter("to_id");
        String pid = req.getParameter("pid");
        int friend_type = Integer.parseInt(req.getParameter("friend_type"));
        int source = Integer.parseInt(req.getParameter("source"));
        resp.getWriter().write(service.addFriend(to_id, from_id,pid,friend_type,source));
    }
}
