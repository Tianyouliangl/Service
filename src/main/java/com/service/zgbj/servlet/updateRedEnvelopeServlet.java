package com.service.zgbj.servlet;

import com.service.zgbj.im.RedEnvelopeBean;
import com.service.zgbj.mysqlTab.controller.ChatServiceImpl;
import com.service.zgbj.mysqlTab.controller.HistoryServiceImpl;
import com.service.zgbj.mysqlTab.controller.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/updateRedEnvelope/user")
public class updateRedEnvelopeServlet extends HttpServlet {

    @Autowired
    private ChatServiceImpl chatService;
    @Autowired
    private HistoryServiceImpl historyService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pid = req.getParameter("pid");
        String fromId = req.getParameter("fromId");
        String toId = req.getParameter("toId");
        historyService.updateHistoryStatus(fromId,RedEnvelopeBean.STATUS_ALREADY_RECEIVED,pid);
        historyService.updateHistoryStatus(toId,RedEnvelopeBean.STATUS_ALREADY_RECEIVED,pid);
        chatService.updateRedEnvelope(RedEnvelopeBean.STATUS_ALREADY_RECEIVED, pid);
        String json = chatService.getRedEnvelopePid(pid);
        resp.getWriter().write(json);
    }
}
