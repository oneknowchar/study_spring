package com.demoWebsocket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.demoWebsocket.web.mapper")
public class DemoWebsocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoWebsocketApplication.class, args);
	}

}
