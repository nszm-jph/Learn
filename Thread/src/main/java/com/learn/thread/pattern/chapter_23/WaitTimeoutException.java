package com.learn.thread.pattern.chapter_23;

public class WaitTimeoutException extends Exception{

    public WaitTimeoutException(String message) {
        super(message);
    }
}
