package com.learn.thread.pattern.chapter_27.common;

import com.learn.thread.pattern.chapter_19.Future;
import com.learn.thread.pattern.chapter_19.FutureService;
import com.learn.thread.pattern.chapter_27.OrderService;

import java.util.concurrent.TimeUnit;

public class OrderServiceImpl implements OrderService {

    @ActiveMethod
    @Override
    public Future<String> findOrderDetails(long orderId) {
        return FutureService.<Long, String>newServer().submit(input -> {
            try {
                // 通过休眠来模拟该方法的执行比较耗时
                TimeUnit.SECONDS.sleep(10);
                System.out.println("process the orderID- >" + input);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "The order Details information";
        }, orderId, null);
    }

    @ActiveMethod
    @Override
    public void order(String account, long orderId) {
        try {
            TimeUnit.SECONDS.sleep(10);
            System.out.println("process the order for account " + account + ", orderId " + orderId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
