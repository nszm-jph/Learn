package com.learn.thread.pattern.chapter_27.common;

import com.learn.thread.pattern.chapter_19.Future;
import com.learn.thread.pattern.chapter_27.ActiveFuture;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ActiveServiceFactory {

    // 定义ActiveMessageQueue ，用于存放ActiveMessage
    private final static ActiveMessageQueue queue= new ActiveMessageQueue();

    public static <T> T active(T instance) {
        // 生成Service 的代理类
        Object proxy = Proxy.newProxyInstance(instance.getClass().getClassLoader(), instance.getClass().getInterfaces(),
                new ActiveInvocationHandler<>(instance));
        return (T) proxy;
    }

    // ActiveInvocationHandler 是InvocationHandler 的子类，生成Proxy 时需要使用到
    private static class ActiveInvocationHandler<T> implements InvocationHandler {
        private final T instance;

        public ActiveInvocationHandler(T instance) {
            this.instance = instance;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 如果接口方法被 @ActiveMessage标记， 则会转换为ActiveMessage
            if (method.isAnnotationPresent(ActiveMethod.class)) {
                // 检查该方法是否符合规范
                this.checkMethod(method);
                ActiveMessage.Builder builder = new ActiveMessage.Builder();
                builder.useMethod(method).withObjects(args).forService(instance);
                Object result = null;
                if (this.isReturnFutureType(method)) {
                    result = new ActiveFuture<>();
                    builder.returnFuture((ActiveFuture) result);
                }
                // 将ActiveMessage 加入至队列中
                queue.offer(builder.build());
                return result;
            } else {
                // 如果是普通方法（ 没有使用自ActiveMethod 标记），则会正常执行
                return method.invoke(instance, args);
            }
        }

        // 检查有返回值的方法是否为Future ，否则将会抛出IllegalActiveMethod 异常
        private void checkMethod (Method method) throws IllegalActiveMethod {
            // 有返回值， 必须是ActiveFuture 类型的返回值
            if (!isReturnFutureType(method) && !isReturnVoidType(method)) {
                throw new IllegalActiveMethod("the method [" + method.getName() + " return type must be void/Future");
            }
        }

        // 判断方法是否为Future 返回类型
        private boolean isReturnFutureType(Method method) {
            return method.getReturnType().isAssignableFrom(Future.class);
        }

        // 判断方法是否元返回类型
        private boolean isReturnVoidType(Method method) {
            return method.getReturnType().equals(Void.TYPE);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        OrderServiceImpl active = active(new OrderServiceImpl());
        Future<String> orderDetails = active.findOrderDetails(123);
        System.out.println("i will be returned immediately");
        System.out.println(orderDetails.get());
    }
}
