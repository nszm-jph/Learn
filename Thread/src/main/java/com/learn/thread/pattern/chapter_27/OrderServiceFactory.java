package com.learn.thread.pattern.chapter_27;

public class OrderServiceFactory {

    // 将ActiveMessageQueue 定义成static的目的是，保持其在整个JVM进程中是唯一的，并且
    // ActiveDaemonThread会在此刻启动
    private static final ActiveMessageQueue ACTIVEMESSAGEQUEUE = new ActiveMessageQueue();

    // 不允许外部通过new 的方式构建
    private OrderServiceFactory() {
    }

    // 返回OrderServiceProxy
    public static OrderService toActiveObject(OrderService orderService) {
        return new OrderServiceProxy(orderService, ACTIVEMESSAGEQUEUE);
    }

    public static void main(String[] args) throws InterruptedException {
        // 在创建OrderService 时需要传递OrderService 接口的具体实现
        OrderService orderService = OrderServiceFactory.toActiveObject(new OrderServiceImpl());
        orderService.order("hello", 123456);
        System.out.println("Return Immediately");

        Thread.currentThread().join();
    }
}
