package com.learn.thread.pattern.chapter_nineteen;

import java.util.concurrent.TimeUnit;

public class TestFuture {
    public static void main(String[] args) throws InterruptedException {
        FutureService<Void, Void> futureService = FutureService.newServer();

        Future<?> future = futureService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("I am finish done.");
        });
        future.get();

        FutureService<String, Integer> service = FutureService.newServer();
        Future<Integer> submit = service.submit(input -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return input.length();
        }, "Hello", System.out::println);
    }
}
