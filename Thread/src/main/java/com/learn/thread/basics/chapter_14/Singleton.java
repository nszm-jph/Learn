package com.learn.thread.basics.chapter_14;

public class Singleton {

    private Singleton() {

    }

    private enum EnumHolder {
        INSTANCE;

        private Singleton instance;

        EnumHolder() {
            this.instance = new Singleton();
        }

        private Singleton getSingleton() {
            return instance;
        }
    }

    public Singleton getSingleton() {
        return EnumHolder.INSTANCE.getSingleton();
    }
}
