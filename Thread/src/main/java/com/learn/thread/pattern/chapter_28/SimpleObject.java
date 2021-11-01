package com.learn.thread.pattern.chapter_28;

public class SimpleObject {

    /**
     * subscribe 方法， 比如使用＠Subscribe 标记， 并且是void 类型且有一个参数
     */
    @Subscribe(topic = "alex-topic")
    public void test2(Integer x) {

    }

    @Subscribe(topic = "test-topic")
    public void test3(Integer x) {

    }
}
