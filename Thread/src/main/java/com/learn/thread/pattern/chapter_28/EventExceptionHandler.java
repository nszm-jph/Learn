package com.learn.thread.pattern.chapter_28;

public interface EventExceptionHandler {
    void handle(Throwable cause, EventContext eventContext);
}
