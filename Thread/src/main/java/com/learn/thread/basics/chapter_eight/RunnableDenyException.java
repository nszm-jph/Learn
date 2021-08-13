package com.learn.thread.basics.chapter_eight;

/**
 * 用于通知任务提交者，任务队列已无法再接收新的任务
 * @author jph
 */
public class RunnableDenyException extends RuntimeException{

    public RunnableDenyException(String message) {
        super(message);
    }
}
