package com.learn.thread.pattern.chapter_26;

public interface InstructionBook {
    
    default void create() {
        this.firstProcess();
        this.secondProcess();
    }

    void firstProcess();
    void secondProcess();
}
