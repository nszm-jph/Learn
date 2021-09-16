package com.learn.thread.pattern.chapter_25;

@FunctionalInterface
public interface CacheLoader<K, V> {

    V load(K k);
}
