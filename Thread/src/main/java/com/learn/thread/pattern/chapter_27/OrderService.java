package com.learn.thread.pattern.chapter_27;

import com.learn.thread.pattern.chapter_19.Future;

public interface OrderService {

    /**
     * 根据订单编号查询订单明细， 有入参也有返回值，但是返回类型必须是Future
     */
    Future<String> findOrderDetails(long orderId);

    /**
     * 提交订单， 没有返回值
     */
    void order(String account, long orderId);
}
