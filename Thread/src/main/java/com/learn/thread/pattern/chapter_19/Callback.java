package com.learn.thread.pattern.chapter_19;

@FunctionalInterface
public interface Callback<T> {
    void call(T t);
}
