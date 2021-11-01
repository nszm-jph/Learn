package com.learn.thread.pattern.chapter_28;

public class SimpleSubscriber {

    @Subscribe
    public void method1(String message) {
        System.out.println("=SimpleSubscriber1==method1=" + message);
    }

    @Subscribe(topic = "test")
    public void method2(String message) {
        System.out.println("=SimpleSubscriber1==method2=" + message);
    }

    public static void main(String[] args) {
        Bus bus = new EventBus ("TestBus");
        bus.register(new SimpleSubscriber());
        bus.post (" Hello ");
        System.out.println ("------------");
        bus.post (" Hello ","test");
    }
}
