package com.learn.thread.basics.chapter_five;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.in;

public class BooleanLock implements Lock{

    private Thread currentThread;

    private boolean locked = false;

    private final List<Thread> blockedList = new ArrayList<>();

    @Override
    public void lock() throws InterruptedException {
        synchronized (this) {
            while (locked) {
                try {
                    if(!blockedList.contains(Thread.currentThread())) {
                        blockedList.add(Thread.currentThread());
                    }
                    this.wait();
                } catch (InterruptedException interruptedException) {
                    // 线程被中断时，移除相应线程，防止内存溢出
                    blockedList.remove(Thread.currentThread());
                    throw interruptedException;
                }

            }
            blockedList.remove(Thread.currentThread());
            this.locked = true;
            this.currentThread = Thread.currentThread();
        }
    }

    @Override
    public void lock(long mills) throws InterruptedException, TimeoutException {
        synchronized (this) {
            if (mills < 0) {
                this.lock();
            }
            else {
                long remainingMills = mills;
                long endMills = currentTimeMillis() + remainingMills;
                while (locked) {
                    if (remainingMills < 0) {
                        throw new TimeoutException ("can not get the lock during " + mills);
                    }
                    if (!blockedList.contains(Thread.currentThread())) {
                        blockedList.add(Thread.currentThread());
                    }
                    this.wait(remainingMills);
                    remainingMills = endMills - currentTimeMillis();
                }
                blockedList.remove(Thread.currentThread());
                this.locked = true;
                this.currentThread = Thread.currentThread();
            }
        }
    }

    @Override
    public void unlock() {
        synchronized (this) {
            if (currentThread == Thread.currentThread()) {
                this.locked = false;
                this.notifyAll();
            }
        }
    }

    @Override
    public List<Thread> getBlockedThreads() {
        return Collections.unmodifiableList(blockedList);
    }
}
