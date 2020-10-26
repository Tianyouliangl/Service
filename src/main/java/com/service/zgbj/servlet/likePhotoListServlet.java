package com.service.zgbj.servlet;

import com.service.zgbj.mysqlTab.controller.FileImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fengzhangwei on 2020/10/21.
 */
@WebServlet("/photo/likePhotoList")
public class likePhotoListServlet extends HttpServlet {
    @Autowired
    private FileImpl fileService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uid = req.getParameter("uid");
        resp.getWriter().write(fileService.getLikePhoto(uid));
    }
}
