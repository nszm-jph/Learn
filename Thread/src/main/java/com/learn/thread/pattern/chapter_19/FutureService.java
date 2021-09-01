package com.learn.thread.pattern.chapter_19;


public interface FutureService<IN, OUT> {

    /**
     * 提交不需要返回值的任务， Future.get 方法返回的将会是null
     * @param runnable
     * @return
     */
    Future<?> submit(Runnable runnable);

    /**
     * 提交需要返回值的任务． 其中Task 接口代替了Runnable 接口
     * @param task
     * @param input
     * @return
     */
    Future<OUT> submit(Task<IN, OUT> task, IN input, Callback<OUT> callback);

    /**
     * 使用静态方法创建一个FutureService 的实现
     * @param <IN>
     * @param <OUT>
     * @return
     */
    static <IN, OUT> FutureService<IN, OUT> newServer() {
        return new FutureServiceImpl<>();
    }
}
