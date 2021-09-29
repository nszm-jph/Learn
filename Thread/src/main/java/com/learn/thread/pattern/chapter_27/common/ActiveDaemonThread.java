package com.learn.thread.pattern.chapter_27.common;


public class ActiveDaemonThread extends Thread {

    private final ActiveMessageQueue queue;

    public ActiveDaemonThread(ActiveMessageQueue queue) {
        super("ActiveDaemonThread");
        this.queue = queue;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            /*
            从MethodMessage 队列中获取一个MethodMessage ， 然后执行execute 方法
             */
            ActiveMessage methodMessage = queue.take();
            methodMessage.execute();
        }
    }
}
