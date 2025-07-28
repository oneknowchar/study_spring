package com.demoCache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan("com.demoCache.web.mapper")
public class DemoCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoCacheApplication.class, args);
    }

}
