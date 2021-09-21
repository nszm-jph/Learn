package com.learn.thread.pattern.chapter_26;

public class Production implements InstructionBook{

    // 产品编号
    private final int prodID;

    public Production(int prodID) {
        this.prodID = prodID;
    }

    @Override
    public void firstProcess() {
        System.out.println("execute the " + prodID + " first process");
    }

    @Override
    public void secondProcess() {
        System.out.println("execute the " + prodID + " second process");
    }
}
