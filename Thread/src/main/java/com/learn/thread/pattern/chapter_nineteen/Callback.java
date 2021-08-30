package com.learn.thread.pattern.chapter_nineteen;

@FunctionalInterface
public interface Callback<T> {
    void call(T t);
}
