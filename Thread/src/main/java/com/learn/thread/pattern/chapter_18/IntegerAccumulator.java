package com.learn.thread.pattern.chapter_18;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public final class IntegerAccumulator {
    private final int init;

    public IntegerAccumulator(int init) {
        this.init = init;
    }

    public IntegerAccumulator(IntegerAccumulator integerAccumulator, int i) {
        this.init = integerAccumulator.getValue() + i;
    }

    public int getValue() {
        return init;
    }

    public IntegerAccumulator add(int i) {
        return new IntegerAccumulator(this, i);
    }

    public static void main(String[] args) {
        IntegerAccumulator integerAccumulator = new IntegerAccumulator(0);

        IntStream.range(0, 3).forEach(i -> new Thread(() -> {
            int inc = 0;
            while (true) {
                int value = integerAccumulator.getValue();
                int result = integerAccumulator.add(inc).getValue();
                System.out.println(value + "+" + inc + "=" + result);
                if (value + inc != result) {
                    System.out.println("ERROR :" + value + "+" + inc + "=" + result);
                }
                inc++;
                slowly();
            }
        }));
    }

    private static void slowly() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
