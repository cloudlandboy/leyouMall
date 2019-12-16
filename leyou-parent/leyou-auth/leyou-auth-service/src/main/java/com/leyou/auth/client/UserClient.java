package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author cloudlandboy
 * @Date 2019/12/16 下午4:58
 * @Since 1.0.0
 */

@FeignClient("user-service")
public interface UserClient extends UserApi {

}