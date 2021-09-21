package com.learn.thread.pattern.chapter_26;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Worker extends Thread{

    private final ProductionChannel channel;

    // 主要用于获取一个随机值，模拟加工一个产品需要耗费一定的时间，当然每个工人操作时所花费的时间可也能不一样
    private final static Random random = new Random(System.currentTimeMillis());

    public Worker(String workerName, ProductionChannel channel) {
        super(workerName);
        this.channel = channel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Production production = channel.takeProduction();
                System.out.println(getName() + " process the" +production);
                // 对产品进行加工
                production.create();
                TimeUnit.SECONDS.sleep(random.nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ProductionChannel productionChannel = new ProductionChannel(5);
        AtomicInteger productionNo = new AtomicInteger();
        // 流水线上有8 个工作人员往传送带上不断地放置等待加工的半成品
        IntStream.range(1, 8).forEach(i -> {
            new Thread(() -> {
                while (true) {
                    productionChannel.offerProduction(new Production(productionNo.getAndIncrement()));
                    try {
                        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        });
    }
}
