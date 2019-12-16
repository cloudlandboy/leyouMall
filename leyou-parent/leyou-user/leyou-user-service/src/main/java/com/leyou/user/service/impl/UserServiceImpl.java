package com.leyou.user.service.impl;

import com.leyou.common.util.CodecUtils;
import com.leyou.common.util.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user:code:phone:";


    @Override
    public Boolean checkData(String data, Integer type) {
        User user = new User();
        if (type == 1) {
            //校验用户名
            user.setUsername(data);
        } else if (type == 2) {
            //校验手机
            user.setPhone(data);
        }
        //查询
        int count = this.userMapper.selectCount(user);
        return count == 0;
    }

    @Override
    public Boolean sendVerifyCode(String phone) {
        //校验手机号格式是否正确
        if (!phone.matches("^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$")) {
            return false;
        }

        //生成验证码
        String code = NumberUtils.generateCode(6);
        Map<String, String> msg = new HashMap<>(2);
        msg.put("phone", phone);
        msg.put("code", code);

        try {
            //将验证码存入redis，有效期5分钟
            redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
            //调用sms服务发送验证码
            amqpTemplate.convertAndSend("LEYOU.SMS.EXCHANGE", "sms.verify.code", msg);
        } catch (AmqpException e) {
            LOGGER.error("给{}发送验证码失败", phone, e);
            return null;
        }

        return true;
    }

    @Override
    public Boolean register(User user, String code) {
        //校验验证码是否正确
        String realCode = this.redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if (!StringUtils.equals(realCode, code)) {
            return false;
        }

        //生成盐
        String salt = CodecUtils.generateSalt();
        //设置盐
        user.setSalt(salt);
        //密码加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));

        // 强制设置不能指定的参数为null
        user.setId(null);
        user.setCreated(new Date());

        //保存到数据库
        boolean boo = this.userMapper.insertSelective(user) == 1;

        //注册成功删除redis中的验证码
        if (boo) {
            this.redisTemplate.delete(KEY_PREFIX + user.getPhone());
        }

        return boo;
    }

    @Override
    public User queryUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        //先根据用户名查询用户
        user = this.userMapper.selectOne(user);
        if (user == null) {
            //没有用户直接返回null
            return null;
        }

        //查询到用户进行密码比对
        boolean boo = StringUtils.equals(user.getPassword(), CodecUtils.md5Hex(password, user.getSalt()));

        if (!boo) {
            //密码不一致，返回null
            return null;
        }

        return user;
    }
}