package com.service.zgbj.mysqlTab.controller;

import com.service.zgbj.im.ChatMessage;
import com.service.zgbj.im.RedEnvelopeBean;
import com.service.zgbj.mysqlTab.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DataBaseImpl implements DataBaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void displayDataBase() {
        String createSql = "CREATE DATABASE IF NOT EXISTS mysql DEFAULT CHARACTER SET utf8";
        int code = jdbcTemplate.update(createSql);
        if (code > 0) {
            System.out.println("数据库---mysql----创建!");
        }
    }
}
