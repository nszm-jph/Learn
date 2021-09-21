package com.learn.thread.pattern.chapter_27;

import java.util.Map;

public class OrderMessage extends MethodMessage {

    public OrderMessage(Map<String, Object> params, OrderService orderService) {
        super(params, orderService);
    }

    @Override
    public void execute() {
        String account = (String) params.get("account");
        Long orderId = (Long) params.get("orderId");

        orderService.order(account, orderId);
    }
}
