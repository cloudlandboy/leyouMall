package com.leyou.order.interceptor;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.util.JwtUtils;
import com.leyou.common.util.CookieUtils;
import com.leyou.order.config.JwtProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author cloudlandboy
 * @Date 2019/12/17 下午4:02
 * @Since 1.0.0
 */

public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();

    private JwtProperties jwtProperties;

    public LoginInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取token
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());

        if (StringUtils.isBlank(token)) {
            //未携带token，不放行,返回未认证
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        UserInfo userInfo = null;

        try {
            //根据token获取用户信息
            userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
        } catch (Exception e) {
            //token超时、被篡改，或者其他异常
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        //当用户信息放入threadlocal
        THREAD_LOCAL.set(userInfo);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //请求返回后，需要手动清处threadlocal中的数据，因为使用的线程是连接池中的只会归还不会被清理
        THREAD_LOCAL.remove();
    }

    /**
     * 提供获取UserInfo的方法
     *
     * @return
     */
    public static UserInfo getLoginUser() {
        return THREAD_LOCAL.get();
    }
}