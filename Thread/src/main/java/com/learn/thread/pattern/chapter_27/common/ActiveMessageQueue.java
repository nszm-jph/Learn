package com.learn.thread.pattern.chapter_27.common;

import java.util.LinkedList;

public class ActiveMessageQueue {

    // 用于存放提交的MethodMessage 消息
    private final LinkedList<ActiveMessage> messages = new LinkedList<>();

    public ActiveMessageQueue() {
        // 启动Worker 线程
        new ActiveDaemonThread(this).start();
    }

    public void offer(ActiveMessage methodMessage) {
        synchronized (this) {
            messages.addLast(methodMessage);
            // 因为只有一个线程负责take 数据， 因此没有必要使用notifyAll 方法
            notify();
        }
    }

    public ActiveMessage take() {
        synchronized (this) {
            // 当Method Message 队列中没有Message 的时候， 执行线程进入阻塞
            while (messages.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 获取其中一个MethodMessage 并且从队列中移除
            return messages.removeFirst();
        }
    }
}
