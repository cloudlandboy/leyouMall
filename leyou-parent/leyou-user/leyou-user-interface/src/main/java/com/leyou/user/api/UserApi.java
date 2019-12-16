package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author cloudlandboy
 * @Date 2019/12/16 下午4:55
 * @Since 1.0.0
 */

public interface UserApi {

    @GetMapping("/query")
    public User queryUser(@RequestParam("username") String username, @RequestParam("password") String password);
}