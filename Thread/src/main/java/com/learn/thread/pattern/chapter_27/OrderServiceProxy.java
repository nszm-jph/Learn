package com.learn.thread.pattern.chapter_27;

import com.learn.thread.pattern.chapter_19.Future;

import java.util.HashMap;

public class OrderServiceProxy implements OrderService{

    private final OrderService orderService;

    private final ActiveMessageQueue activeMessageQueue;

    public OrderServiceProxy(OrderService orderService, ActiveMessageQueue activeMessageQueue) {
        this.orderService = orderService;
        this.activeMessageQueue = activeMessageQueue;
    }

    @Override
    public Future<String> findOrderDetails(long orderId) {
        // 定义一个ActiveFuture ，并且可支持立即返回
        ActiveFuture<String> activeFuture = new ActiveFuture<>();
        // 收集方法入参以及返回的ActiveFuture 封装成MethodMessage
        HashMap<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("activeFuture", activeFuture);
        MethodMessage message = new FindOrderDetailsMessage(params, orderService);

        // 将MethodMessage 保存至activeMessageQueue 中
        activeMessageQueue.offer(message);
        return activeFuture;
    }

    @Override
    public void order(String account, long orderId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("orderId", orderId);
        MethodMessage orderMessage = new OrderMessage(params, orderService);
        activeMessageQueue.offer(orderMessage);
    }
}
