package com.leyou.order.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.order.pojo.Order;

/**
 * @Author cloudlandboy
 * @Date 2019/12/19 上午9:56
 * @Since 1.0.0
 */

public interface OrderService {

    /**
     * 创建订单
     *
     * @param order
     * @return
     */
    Long createOrder(Order order);

    /**
     * 根据订单id查询订单
     *
     * @param id
     * @return
     */
    Order queryById(Long id);

    /**
     * 分页查询登录用户订单
     *
     * @param page
     * @param rows
     * @param status
     * @return
     */
    PageResult<Order> queryUserOrderList(Integer page, Integer rows, Integer status);

    /**
     * 更新订单状态
     *
     * @param id
     * @param status
     * @return
     */
    Boolean updateStatus(Long id, Integer status);
}