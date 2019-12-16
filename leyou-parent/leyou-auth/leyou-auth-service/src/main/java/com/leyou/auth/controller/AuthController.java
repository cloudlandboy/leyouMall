package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.util.JwtUtils;
import com.leyou.common.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author cloudlandboy
 * @Date 2019/12/16 下午4:46
 * @Since 1.0.0
 */

@Controller
@EnableConfigurationProperties({JwtProperties.class})
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录授权
     *
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/accredit")
    public ResponseEntity<Void> authentication(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response) {

        String token = this.authService.authentication(username, password);

        if (StringUtils.isBlank(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
        CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token,
                jwtProperties.getExpire() * 60, null, true);

        return ResponseEntity.ok(null);
    }

    /**
     * 验证用户信息
     *
     * @param token
     * @return
     */
    @GetMapping("/verify")
    public ResponseEntity<UserInfo> verifyUser(@CookieValue("${leyou.jwt.cookieName}") String token,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws Exception {
        UserInfo userInfo = this.authService.verifyUser(token);
        if (userInfo == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        //刷新token
        token = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());

        // 将token重新写入cookie
        CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token,
                jwtProperties.getExpire() * 60, null, true);

        return ResponseEntity.ok(userInfo);
    }
}