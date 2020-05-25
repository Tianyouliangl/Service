package com.service.zgbj;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.service.zgbj.mysqlTab.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.net.InetAddress;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.service.zgbj.servlet")
public class ZgbjApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        System.out.println("Sconfigure ------------------------------.");
        return builder.sources(ZgbjApplication.class);
    }

    public static void main(String[] args) {

        try {
            SpringApplication.run(ZgbjApplication.class, args);
            System.out.println("Server startup done.");
        } catch (Exception e) {
            System.out.println("服务xxx-support启动报错\n" + e);
        }
    }

    /**
     * 注册netty-socketio服务端
     */
    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        // 重新启动端口被占用
        SocketConfig socketConfig = config.getSocketConfig();
        socketConfig.setReuseAddress(true);
        InetAddress ia = null;
        try {
            ia = ia.getLocalHost();
            String localname = ia.getHostName();
            String localip = ia.getHostAddress();
            System.out.println("本机名称是：" + localname);
            System.out.println("本机的ip是 ：" + localip);
            config.setHostname(localip);
        } catch (Exception e) {
            System.out.println("获取本地信息失败～：" + e.toString());
            e.printStackTrace();
        }

        config.setPort(9092);
        System.out.println("SocketIOServer ====== " + config.getHostname());
        config.setAuthorizationListener(new AuthorizationListener() {//类似过滤器
                                            @Override
                                            public boolean isAuthorized(HandshakeData data) {
                                                //http://localhost:8081?username=test&password=test
                                                //例如果使用上面的链接进行connect，可以使用如下代码获取用户密码信息，本文不做身份验证
                                                // String username = data.getSingleUrlParam("username");
                                                // String password = data.getSingleUrlParam("password");
                                                String uid = data.getSingleUrlParam("uid");
                                                System.out.println("SocketIOServer uid====== " + uid);
                                                return true;
                                            }
                                        }

        );
        final SocketIOServer server = new SocketIOServer(config);
        return server;
    }

    /**
     * tomcat启动时候，扫码socket服务器并注册
     *
     * @param socketServer
     * @return
     */
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }
}
