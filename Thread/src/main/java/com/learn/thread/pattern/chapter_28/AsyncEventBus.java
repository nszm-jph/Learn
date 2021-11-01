package com.learn.thread.pattern.chapter_28;

import java.util.concurrent.ThreadPoolExecutor;

public class AsyncEventBus extends EventBus{

    public AsyncEventBus(String busName, EventExceptionHandler exceptionHandler, ThreadPoolExecutor executor) {
        super(busName, exceptionHandler, executor);
    }

    public AsyncEventBus(String busName, ThreadPoolExecutor executor) {
        this(busName, null, executor);
    }

    public AsyncEventBus(ThreadPoolExecutor executor) {
        this("default-async", null, executor);
    }

    public AsyncEventBus(EventExceptionHandler eventExceptionHandler, ThreadPoolExecutor executor) {
        this("default-async", eventExceptionHandler, executor);
    }
}
