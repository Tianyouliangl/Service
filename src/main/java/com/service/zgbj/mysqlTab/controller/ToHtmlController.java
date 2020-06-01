package com.service.zgbj.mysqlTab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by fengzhangwei on 2020/6/1.
 */
@Controller
public class ToHtmlController {
    @RequestMapping("/upload")
    public String upload(){
        return "upload";
    }
}
