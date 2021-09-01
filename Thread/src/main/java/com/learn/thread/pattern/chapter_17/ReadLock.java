package com.learn.thread.pattern.chapter_17;

public class ReadLock implements Lock{

    private final ReadWriteLockImpl readWriteLock;

    public ReadLock(ReadWriteLockImpl readWriteLock) {
        this.readWriteLock = readWriteLock;
    }

    @Override
    public void lock() throws InterruptedException {
        synchronized (this.readWriteLock.getMUTEX()) {
            // 若此时有线程在进行写操作，或者有写线程在等待并且偏向写锁的标识为true时，就会无法获得读锁，只能被挂起
            while (this.readWriteLock.getWritingWriters() > 0
                    || (this.readWriteLock.isPreferWriter()
                    && this.readWriteLock.getWaitingWriters() > 0)) {
                readWriteLock.getMUTEX().wait();
            }
            this.readWriteLock.incrementReadingReaders();
        }
    }

    @Override
    public void unlock() {
        synchronized (this.readWriteLock.getMUTEX()) {
            // 释放锁的过程就是使得当前reading 的数量减一
            // 将preferWriter设置为true，可以使得writer线程获得更多的机会
            // 通知唤醒与Mutex关联monitor waitset中的线程
            this.readWriteLock.decrementReadingReaders();
            this.readWriteLock.changePrefer(true);
            this.readWriteLock.getMUTEX().notifyAll();
        }
    }
}
