package com.learn.thread.basics.chapter_three;

import java.util.concurrent.TimeUnit;

public class ThreadisInterrupted {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread() {
            @Override
            public void run () {
                while (true) {

                }
            }
        };

        thread1.setDaemon(true);
        thread1.start();
        TimeUnit.MILLISECONDS.sleep(2);
        System.out.printf("Thread is interrupted? %s \n", thread1.isInterrupted());
        thread1.interrupt();
        TimeUnit.MILLISECONDS.sleep(2);
        System.out.printf("Thread is interrupted? %s \n", thread1.isInterrupted());

        Thread thread2 = new Thread() {
            @Override
            public void run () {
                while (true) {
                    try {
                        TimeUnit.MINUTES.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread2.setDaemon(true);
        thread2.start();
        TimeUnit.MILLISECONDS.sleep(2);
        System.out.printf("Thread is interrupted? %s \n", thread2.isInterrupted());
        thread2.interrupt();
        TimeUnit.MILLISECONDS.sleep(2);
        System.out.printf("Thread is interrupted? %s \n", thread2.isInterrupted());
    }
}
