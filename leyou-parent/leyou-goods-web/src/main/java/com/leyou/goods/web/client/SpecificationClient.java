package com.leyou.goods.web.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface SpecificationClient extends SpecificationApi {
}