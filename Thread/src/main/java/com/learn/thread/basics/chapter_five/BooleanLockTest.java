package com.learn.thread.basics.chapter_five;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BooleanLockTest {

    private final Lock lock = new BooleanLock();

    public void syncMethod() {
        try {
            lock.lock();
            int randomInt = ThreadLocalRandom.current().nextInt(10);
            System.out.println(Thread.currentThread() + " get the lock. ");
            TimeUnit.SECONDS.sleep(randomInt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BooleanLockTest booleanLockTest = new BooleanLockTest();

//        IntStream.range(0, 10).mapToObj(i -> new Thread(booleanLockTest::syncMethod))
//                .forEach(Thread::start);
        new Thread(booleanLockTest::syncMethod).start();
        TimeUnit.MILLISECONDS.sleep(2);

        Thread t1 = new Thread(booleanLockTest::syncMethod);
        t1.start();
        TimeUnit.MILLISECONDS.sleep(10);
        t1.interrupt();
    }
}
