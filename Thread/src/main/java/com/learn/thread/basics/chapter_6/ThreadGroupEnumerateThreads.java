package com.learn.thread.basics.chapter_6;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class ThreadGroupEnumerateThreads {

    public static void main(String[] args) {
        ThreadGroup threadGroup = new ThreadGroup("threadGroup");

        Thread thread = new Thread(threadGroup, () -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "MyThread");
        thread.start();

        ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();
        Thread[] threads = new Thread[mainGroup.activeCount()];
        System.out.println(mainGroup.enumerate(threads));
        Arrays.stream(threads).forEach(System.out::println);

        System.out.println(mainGroup.enumerate(threads, false));

        // 通过设置组的优先级，可以让组中的现有线程的优先级大于组的优先级
        System.out.println(threadGroup.getMaxPriority());
        System.out.println(thread.getPriority());
        threadGroup.setMaxPriority(3);
        System.out.println(threadGroup.getMaxPriority());
        System.out.println(thread.getPriority());
    }
}
