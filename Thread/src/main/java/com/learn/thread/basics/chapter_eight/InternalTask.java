package com.learn.thread.basics.chapter_eight;

/**
 * 主要用于线程池内部，该类会使用到RunnableQueue，
 * 然后不断地从queue中取出某个runnable，并运行runnable的run方法
 * @author jph
 */
public class InternalTask implements Runnable{
    private final RunnableQueue runnableQueue;

    private volatile boolean running = true;

    public InternalTask(RunnableQueue runnableQueue) {
        this.runnableQueue = runnableQueue;
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Runnable take = runnableQueue.take();
                take.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.running = false;
    }
}
