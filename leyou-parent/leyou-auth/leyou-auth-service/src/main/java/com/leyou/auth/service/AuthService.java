package com.leyou.auth.service;

import com.leyou.auth.pojo.UserInfo;

/**
 * @Author cloudlandboy
 * @Date 2019/12/16 下午4:48
 * @Since 1.0.0
 */

public interface AuthService {

    /**
     * 登录授权生成token返回
     *
     * @param username
     * @param password
     * @return
     */
    String authentication(String username, String password);

    /**
     * 验证用户信息
     * @param token
     * @return
     */
    UserInfo verifyUser(String token) throws Exception;
}