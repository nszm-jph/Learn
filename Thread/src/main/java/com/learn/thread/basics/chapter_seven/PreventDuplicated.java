package com.learn.thread.basics.chapter_seven;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class PreventDuplicated {
    private final static String LOCK_PATH = ".\\";

    private final static String LOCK_FILE = ".lock";

    private static void checkRunning() throws IOException {
        Path lockFile = getLockFile();

        if (lockFile.toFile().exists()) {
            throw new RuntimeException("The program already running .");
        }

        Files.createFile(lockFile);
    }

    private static Path getLockFile() {
        return Paths.get(LOCK_PATH, LOCK_FILE);
    }

    public static void main(String[] args) throws IOException {
        checkRunning();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("The program received kill SIGNAL.");
            getLockFile().toFile().delete();
        }));

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("program is running.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
