package com.service.zgbj.servlet;

import com.service.zgbj.mysqlTab.controller.HistoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fengzhangwei on 2020/10/14.
 */
@WebServlet("/chat/getConversation")
public class conversationServlet extends HttpServlet {
    @Autowired
    private HistoryServiceImpl service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fromId = req.getParameter("fromId");
        String toId = req.getParameter("toId");
        resp.getWriter().write(service.getConversation(fromId,toId));
    }
}
