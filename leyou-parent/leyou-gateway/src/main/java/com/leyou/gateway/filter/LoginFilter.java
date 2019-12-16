package com.leyou.gateway.filter;

import com.leyou.auth.util.JwtUtils;
import com.leyou.common.util.CookieUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/12/16 下午8:04
 * @Since 1.0.0
 */

@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;

    private List<String> allowPaths = null;

    private final Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);

    /**
     * 给构造方法注入
     *
     * @param filterProperties
     */
    public LoginFilter(FilterProperties filterProperties) {
        this.allowPaths = CollectionUtils.isEmpty(filterProperties.getAllowPaths()) ? Collections.emptyList() : filterProperties.getAllowPaths();
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        // 获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = context.getRequest();

        //获取请求路径
        String requestPath = request.getRequestURL().toString();

        for (String allowPath : allowPaths) {
            if (requestPath.contains(allowPath)) {
                //白名单放行
                return false;
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = context.getRequest();
        // 获取token
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());

        //校验
        try {
            // 校验通过什么都不做，即放行
            JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
        } catch (Exception e) {
            // 校验出现异常，返回403,不进行请求转发
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        }
        return null;
    }
}