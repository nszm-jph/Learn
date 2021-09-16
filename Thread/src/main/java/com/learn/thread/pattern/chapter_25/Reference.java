package com.learn.thread.pattern.chapter_25;

public class Reference {

    // 1M
    private final byte[] data = new byte[2 << 9];

    @Override
    protected void finalize() throws Throwable {
        System.out.println("the reference will be GC.");
    }
}
