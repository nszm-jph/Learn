package com.learn.thread.pattern.chapter_nineteen;

public interface Task<IN, OUT> {
    /**
     * 给定一个参数， 经过计算返回结果
     * @param input
     * @return
     */
    OUT get(IN input);
}
