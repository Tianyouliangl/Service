package com.service.zgbj;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.service.zgbj.servlet")
public class ZgbjApplication extends SpringBootServletInitializer{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ZgbjApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(ZgbjApplication.class, args);
	}

	/**
	 * 注册netty-socketio服务端
	 */
	@Bean
	public SocketIOServer socketIOServer() {
		com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();

//		String os = System.getProperty("os.name");
//		if(os.toLowerCase().startsWith("win")){   //在本地window环境测试时用localhost
//			System.out.println("this is  windows");
//			config.setHostname("localhost");
//		} else {
//			config.setHostname("192.168.9.209");
//		}
		config.setHostname("172.16.200.235");
		config.setPort(9092);

        /*config.setAuthorizationListener(new AuthorizationListener() {//类似过滤器
            @Override
            public boolean isAuthorized(HandshakeData data) {
                //http://localhost:8081?username=test&password=test
                //例如果使用上面的链接进行connect，可以使用如下代码获取用户密码信息，本文不做身份验证
                // String username = data.getSingleUrlParam("username");
                // String password = data.getSingleUrlParam("password");
                return true;
            }
        });*/
		final SocketIOServer server = new SocketIOServer(config);
		return server;
	}

	/**
	 * tomcat启动时候，扫码socket服务器并注册
	 * @param socketServer
	 * @return
	 */
	@Bean
	public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
		return new SpringAnnotationScanner(socketServer);
	}
}
