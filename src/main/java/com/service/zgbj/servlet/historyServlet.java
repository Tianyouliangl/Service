package com.service.zgbj.servlet;

import com.service.zgbj.mysqlTab.controller.HistoryServiceImpl;
import com.service.zgbj.mysqlTab.controller.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/history/user")
public class historyServlet extends HttpServlet {
    @Autowired
    private HistoryServiceImpl service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uid = req.getParameter("uid");
        String conversation = req.getParameter("conversation");
        String no = req.getParameter("pageNo");
        String size = req.getParameter("pageSize");
        resp.getWriter().write(service.getChatMessage(uid,conversation,Integer.valueOf(no), Integer.valueOf(size)));
    }
}
