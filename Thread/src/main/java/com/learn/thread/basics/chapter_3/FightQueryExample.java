package com.learn.thread.basics.chapter_3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FightQueryExample {
    private static final List<String> FIGHT_COMPANY = Arrays.asList("CSA", "CEA", "HNA");

    public static void main(String[] args) {
        List<String> results = search("SH", "BJ");
        System.out.println("===============result================");
        results.forEach(System.out::println);
    }

    private static List<String> search(String original, String dest) {
        final List<String> result = new ArrayList<>();

        List<FightQueryTask> tasks = FIGHT_COMPANY.stream()
                .map(f -> createSearchTask(f, original, dest))
                .collect(Collectors.toList());

        tasks.forEach(Thread::start);

        tasks.forEach(t ->
        {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        });

        tasks.stream().map(FightQueryTask::get).forEach(result::addAll);

        return result;
    }

    private static FightQueryTask createSearchTask(String fight, String original, String dest) {
        return new FightQueryTask(fight, original, dest);
    }
}
