package com.service.zgbj.mysqlTab.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.service.zgbj.im.ChatMessage;
import com.service.zgbj.im.RedEnvelopeBean;
import com.service.zgbj.im.SocketManager;
import com.service.zgbj.mysqlTab.DataBaseService;
import com.service.zgbj.utils.OfTenUtils;
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
    @Autowired
    private UserServiceImpl service;
    @Autowired
    private ChatServiceImpl chatService;
    @Autowired
    private HistoryServiceImpl historyService;

    @Override
    public void displayDataBase(SocketIOServer server) {
        String createSql = "CREATE DATABASE IF NOT EXISTS mysql DEFAULT CHARACTER SET utf8";
        int code = jdbcTemplate.update(createSql);
        if (code > 0) {
            System.out.println("数据库---mysql----创建!");
        }
        createTable();
        new SocketManager(server, service, chatService, historyService);
    }

    private void createTable() {
        String sql_user = "CREATE TABLE IF NOT EXISTS " + " table_user " + " (" +
                "id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
                "age INT (11)," +
                "birthday VARCHAR(255)," +
                "email VARCHAR(255) ," +
                "image_url VARCHAR(255)," +
                "location VARCHAR(255)," +
                "mobile VARCHAR(255)," +
                "money FLOAT(11)," +
                "online INT(11)," +
                "password VARCHAR (255)," +
                "sex VARCHAR (255)," +
                "sign VARCHAR(255)," +
                "uid VARCHAR(255)," +
                "username VARCHAR(255)" + ")" +
                "ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET ='utf8mb4'";
        System.out.println("SQL:======" + sql_user);

        String sql_lin = "CREATE TABLE IF NOT EXISTS " + " table_offline_msg " + " (" +
                "id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
                "body TEXT," +
                "body_type INT(11)," +
                "conversation VARCHAR(255)," +
                "from_id VARCHAR(255)," +
                "msg_status INT(11)," +
                "pid VARCHAR(255)," +
                "time BIGINT(11)," +
                "to_id VARCHAR(255)," +
                "type INT(11)," +
                "displaytime INT(11)" + ")" +
                "ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET ='utf8mb4'";
        System.out.println("SQL:======" + sql_lin);

        String sql_red = "CREATE TABLE IF NOT EXISTS " + " table_red_envelope " + " (" +
                "id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
                "body TEXT," +
                "conversation VARCHAR(255)," +
                "from_id VARCHAR(255)," +
                "pid VARCHAR(255)," +
                "status INT(11)," +
                "time BIGINT(11)," +
                "to_id VARCHAR(255)" + ")" +
                "ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET ='utf8mb4'";
        System.out.println("SQL:======" + sql_red);


        String sql_socket = "CREATE TABLE IF NOT EXISTS " + " table_socket" + " (" +
                "id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
                "uid VARCHAR(255)," +
                "token VARCHAR(255)," +
                "mobile VARCHAR(255)" + ")" +
                "ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET ='utf8mb4'";
        System.out.println("SQL:======" + sql_socket);

        jdbcTemplate.update(sql_user);
        jdbcTemplate.update(sql_lin);
        jdbcTemplate.update(sql_red);
        jdbcTemplate.update(sql_socket);



    }
}
