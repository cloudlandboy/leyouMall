package com.leyou.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author cloudlandboy
 * @Date 2019/11/29 下午6:30
 * @Since 1.0.0
 */

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.leyou.item.mapper")
public class LeyouItemServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeyouItemServiceApplication.class, args);
    }
}