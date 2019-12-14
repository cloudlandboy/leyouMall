package com.leyou.upload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author cloudlandboy
 * @Date 2019/12/4 下午6:27
 * @Since 1.0.0
 */

@SpringBootApplication
@EnableDiscoveryClient
public class LeyouUploadApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeyouUploadApplication.class, args);
    }
}