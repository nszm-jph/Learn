package com.learn.thread.pattern.chapter_seventeen;

public interface Lock {

    /**
     * 获取显式锁，没有获得锁的线程将被阻塞
     * @throws InterruptedException
     */
    void lock() throws InterruptedException;

    /**
     * 释放获取的锁
     */
    void unlock();
}
