package com.learn.thread.pattern.chapter_23;

import java.util.concurrent.TimeUnit;

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
    public void await(TimeUnit unit, long time) throws InterruptedException, WaitTimeoutException {
        if (time < 0) {
            throw new IllegalArgumentException("The time is invalid.");
        }

        // 将time 转换为纳秒
        long remainingNanos = unit.toNanos(time);
        // 等待任务将在endNanos 纳秒后超时
        final long endNanos = System.nanoTime() + remainingNanos;
        synchronized (this) {
            while (limit > 0) {
                if (TimeUnit.NANOSECONDS.toMillis(remainingNanos) < 0) {
                    throw new WaitTimeoutException("The wait time over specify time");
                }
                // 等待remainingNanos ，在等待的过程中有可能会被中断，需要重新计算remainingNanos
                this.wait(TimeUnit.NANOSECONDS.toMillis(remainingNanos));
                remainingNanos = endNanos - System.nanoTime();
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
