package com.learn.thread.pattern.chapter_19;

public class FutureTask<T> implements Future {
    // 计算结果
    private T result;

    // 任务是否完成
    private boolean isDone = false;

    // 定义对象锁
    private final Object LOCK = new Object();

    @Override
    public Object get() throws InterruptedException {
        synchronized (LOCK) {
            // 当任务还没完成时， 调用get 方法会被挂起而进入阻塞
            while (!isDone) {
                LOCK.wait();
            }
            // 返回最终计算结果
            return result;
        }
    }

    /**
     * finish 方法主要用于为FutureTask 设置计算结果
     * @param result
     */
    protected void finish(T result) {
        synchronized (LOCK) {
            // balking 设计模式
            if (isDone) {
                return;
            }
            // 计算完成， 为result 指定结果， 并且将isDone 设为true ， 同时唤醒阻塞中的线程
            this.result = result;
            this.isDone = true;
            LOCK.notifyAll();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}
