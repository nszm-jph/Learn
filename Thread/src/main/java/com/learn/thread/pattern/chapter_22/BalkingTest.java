package com.learn.thread.pattern.chapter_22;

public class BalkingTest {

    public static void main(String[] args) {
        new DocumentEditThread("./", "test.txt").start();
    }
}
