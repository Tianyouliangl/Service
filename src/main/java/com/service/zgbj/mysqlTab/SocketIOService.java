package com.service.zgbj.mysqlTab;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * Created by fengzhangwei on 2020/5/14.
 */
@Component
@Order(value=1)
public class SocketIOService implements CommandLineRunner {

    private final SocketIOServer server;

    @Autowired
    private DataBaseService dataBaseService;


    @Autowired
    public SocketIOService(SocketIOServer server) {
        this.server = server;
    }


    @Override
    public void run(String... strings) throws Exception {
        server.start();
        System.out.println("socket.io启动成功！");
        dataBaseService.displayDataBase(server);
    }
}
