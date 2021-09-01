package com.learn.thread.basics.chapter_8;

/**
 * 提供了创建线程的接口
 * @author jph
 */
@FunctionalInterface
public interface ThreadFactory {

    /**
     * 用于创建线程
     * @param runnable
     * @return
     */
    Thread createThread(Runnable runnable);
}
