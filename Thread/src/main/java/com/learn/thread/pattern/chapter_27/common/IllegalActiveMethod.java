package com.learn.thread.pattern.chapter_27.common;

/**
 * 若方法不符合则其被转换为Active 方法时会抛出该异常
 */
public class IllegalActiveMethod extends Exception{

    public IllegalActiveMethod(String message) {
        super(message);
    }
}
