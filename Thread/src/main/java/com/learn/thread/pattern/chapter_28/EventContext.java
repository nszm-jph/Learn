package com.learn.thread.pattern.chapter_28;

import java.lang.reflect.Method;

public interface EventContext {

    String getSource();

    Object getSubscriber();

    Method getSubscribe();

    Object getEvent();
}
