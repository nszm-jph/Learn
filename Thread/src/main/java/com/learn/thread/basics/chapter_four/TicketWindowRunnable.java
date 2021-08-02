package com.learn.thread.basics.chapter_four;

import java.util.concurrent.TimeUnit;

public class TicketWindowRunnable implements Runnable{
    private int index= 1;

    private static final int MAX = 500;

    @Override
    public void run() {
        while (index <= MAX) {
            System.out.println(Thread.currentThread() + " 当前的号码是：" + (index++));
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        TicketWindowRunnable task = new TicketWindowRunnable();

        Thread windowThread1 = new Thread(task, "一号出号机");
        Thread windowThread2 = new Thread(task, "二号出号机");
        Thread windowThread3 = new Thread(task, "三号出号机");
        Thread windowThread4 = new Thread(task, "四号出号机");

        windowThread1.start();
        windowThread2.start();
        windowThread3.start();
        windowThread4.start();
    }
}
