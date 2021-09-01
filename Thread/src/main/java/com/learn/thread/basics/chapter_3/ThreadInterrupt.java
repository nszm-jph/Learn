package com.learn.thread.basics.chapter_3;

import java.util.concurrent.TimeUnit;

public class ThreadInterrupt {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {
                    System.out.println("Oh, i am be interrupted.");
                }
            }
        });

        thread.start();

        TimeUnit.MILLISECONDS.sleep(2);
        System.out. printf ("Thread is interrupted ？%s\n", thread.isInterrupted());
        thread.interrupt();
        TimeUnit.MILLISECONDS.sleep(2);
        System.out. printf ("Thread is interrupted ？%s\n", thread.isInterrupted());
    }
}
