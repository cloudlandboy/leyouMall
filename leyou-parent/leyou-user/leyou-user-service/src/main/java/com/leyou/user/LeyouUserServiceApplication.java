package com.leyou.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author cloudlandboy
 * @Date 2019/12/15 下午6:29
 * @Since 1.0.0
 */

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.leyou.user.mapper")
public class LeyouUserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeyouUserServiceApplication.class, args);
    }
}