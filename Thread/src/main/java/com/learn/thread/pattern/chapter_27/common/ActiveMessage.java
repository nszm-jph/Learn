package com.learn.thread.pattern.chapter_27.common;

import com.learn.thread.pattern.chapter_19.Future;
import com.learn.thread.pattern.chapter_27.ActiveFuture;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 包可见， ActiveMessage 只在框架内部使用， 不会对外暴露
 */
class ActiveMessage {

    // 接口方法的参数
    private final Object[] objects;

    // 接口方法
    private final Method method;

    // 有返回值的方法， 会返回ActiveFuture ＜？＞ 类型
    private final ActiveFuture<Object> activeFuture;

    // 具体的Service 接口
    private final Object service;

    // 构造ActiveMessage 是由Builder 来完成的
    private ActiveMessage(Builder builder) {
        this.activeFuture = builder.future;
        this.objects = builder.objects;
        this.method = builder.method;
        this.service = builder.service;
    }

    // ActiveMessage的方法通过反射的方式调用执行的具体实现
    public void execute() {
        try {
            Object result = method.invoke(service, objects);
            if (activeFuture != null) {
                // 如果是有返回值的接口方法，则需要通过get方法获得最终的结果
                Future<?> realFuture = (Future<?>) result;
                Object realResult = realFuture.get();
                // 将结果交给ActiveFuture，接口方法的线程会得到返回
                activeFuture.finish(realResult);
            }
        } catch (Exception e) {
            // 如果发生异常， 那么有返回值的方法将会显式地指定结果为null ， 无返回值的接口方法则会忽略该异常
            if (activeFuture != null) {
                activeFuture.finish(null);
            }
        }
    }

    static class Builder {
        private Object[] objects;

        private Method method;

        private ActiveFuture<Object> future;

        private Object service;

        public Builder useMethod(Method method) {
            this.method= method;
            return this;
        }

        public Builder returnFuture(ActiveFuture<Object> future) {
            this.future = future;
            return this;
        }

        public Builder withObjects(Object[] objects) {
            this.objects = objects;
            return this;
        }

        public Builder forService(Object service) {
            this.service = service;
            return this;
        }

        public ActiveMessage build() {
            return new ActiveMessage(this);
        }
    }
}
