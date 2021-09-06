package com.learn.thread.pattern.chapter_23;

public class CountDownLatch extends Latch{

    public CountDownLatch(int limit) {
        super(limit);
    }

    @Override
    public void await() throws InterruptedException {
        synchronized (this) {
            // 当limit>O 时， 当前线程进入阻塞状态
            while (limit > 0) {
                this.wait();
            }
        }
    }

    @Override
    public void countDown() {
        synchronized (this) {
            if (limit < 0) {
                throw new IllegalStateException("all of task already arrived");
            }
            // 使limit 减一， 并且通知阻塞线程
            limit--;
            this.notifyAll();
        }
    }

    @Override
    public int getUnarrived() {
        return limit;
    }
}
