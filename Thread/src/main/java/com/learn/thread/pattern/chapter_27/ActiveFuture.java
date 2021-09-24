package com.learn.thread.pattern.chapter_27;

import com.learn.thread.pattern.chapter_19.FutureTask;

public class ActiveFuture<T> extends FutureTask<T> {

    @Override
    public void finish(T result) {
        super.finish(result);
    }
}
