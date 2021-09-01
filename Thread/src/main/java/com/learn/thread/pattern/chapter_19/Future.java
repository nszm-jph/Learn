package com.learn.thread.pattern.chapter_19;

public interface Future<T> {

    /**
     * 返回计算后的结果， 该方法会陷入阻塞状态
     * @return
     * @throws InterruptedException
     */
    T get() throws InterruptedException;

    /**
     * 判断任务是否已经被执行完成
     * @return
     */
    boolean done();
}
