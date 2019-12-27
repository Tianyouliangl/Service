package com.service.zgbj.im;

import com.corundumstudio.socketio.SocketIOServer;

import com.service.zgbj.mysqlTab.controller.ChatServiceImpl;
import com.service.zgbj.mysqlTab.controller.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value=1)
public class ServerRunner implements CommandLineRunner {
    private final SocketIOServer server;

    @Autowired
    private UserServiceImpl service;
    @Autowired
    private ChatServiceImpl chatService;

    @Autowired
    public ServerRunner(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        new SocketManager(server,service,chatService);
        server.start();
        System.out.println("socket.io启动成功！");
    }
}
