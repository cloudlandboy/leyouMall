package com.leyou.user.service;

import com.leyou.user.pojo.User;

/**
 * @Author cloudlandboy
 * @Date 2019/12/15 下午6:21
 * @Since 1.0.0
 */

public interface UserService {

    /**
     * 校验数据是否可用
     *
     * @param data 要校验的数据
     * @param type 要校验的数据类型：1，用户名；2，手机；
     * @return
     */
    Boolean checkData(String data, Integer type);

    /**
     * 发送手机验证码
     *
     * @param phone
     * @return
     */
    Boolean sendVerifyCode(String phone);

    /**
     * 用户注册
     *
     * @param user
     * @param code
     * @return
     */
    Boolean register(User user,String code);

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    User queryUser(String username, String password);
}