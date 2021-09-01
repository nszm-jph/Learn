package com.learn.thread.pattern.chapter_17;

public class ReadWriteLockImpl implements ReadWriteLock{

    // 定义对象锁
    private final Object MUTEX = new Object();

    // 当前有多少个线程正在写入
    private int writingWriters = 0;

    // 当前有多少个线程正在read
    private int readingReaders = 0;

    // 当前有多少个线程正在等待写入
    private int waitingWriters = 0;

    // read和write的偏好设置
    private boolean preferWriter;

    public ReadWriteLockImpl() {
        this(true);
    }

    public ReadWriteLockImpl(boolean preferWriter) {
        this.preferWriter = preferWriter;
    }

    // 获取对象锁
    public Object getMUTEX() {
        return MUTEX;
    }

    // 获取当前是否偏向写锁
    public boolean isPreferWriter() {
        return preferWriter;
    }

    // 设置写锁偏好
    public void changePrefer(boolean preferWriter) {
        this.preferWriter = preferWriter;
    }

    // 使写线程的数量增加
    public void incrementWritingWriters() {
        this.writingWriters++;
    }

    // 使等待写入的线程数量增加
    public void incrementWaitingWriters() {
        this.waitingWriters++;
    }

    // 使读线程的数量增加
    public void incrementReadingReaders() {
        this.readingReaders++;
    }

    // 使写线程的数量减少
    public void decrementWritingWriters() {
        this.writingWriters--;
    }

    // 使等待获取写入锁的数量减一
    public void decrementWaitingWriters() {
        this.waitingWriters--;
    }

    // 使读取线程的数量减少
    public void decrementReadingReaders() {
        this.readingReaders--;
    }

    @Override
    public Lock readLock() {
        return new ReadLock(this);
    }

    @Override
    public Lock writeLock() {
        return new WriteLock(this);
    }

    @Override
    public int getWritingWriters() {
        return this.writingWriters;
    }

    @Override
    public int getWaitingWriters() {
        return this.waitingWriters;
    }

    @Override
    public int getReadingReaders() {
        return this.readingReaders;
    }
}
