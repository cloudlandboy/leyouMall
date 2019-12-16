package com.leyou.auth.service.impl;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.util.JwtUtils;
import com.leyou.user.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author cloudlandboy
 * @Date 2019/12/16 下午4:49
 * @Since 1.0.0
 */

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserClient userClient;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public String authentication(String username, String password) {

        try {
            //调用用户微服务查询用户
            User user = userClient.queryUser(username, password);
            if (user != null) {
                //查询到结果生成token
                UserInfo userInfo = new UserInfo();
                userInfo.setId(user.getId());
                userInfo.setUsername(user.getUsername());
                String token = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
                return token;
            }
        } catch (Exception e) {
            LOGGER.error("生成token出错", e);
        }

        return null;
    }

    @Override
    public UserInfo verifyUser(String token) throws Exception {
        return JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
    }
}