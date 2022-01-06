package com.learn.springbootguides.MessagingWithRedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class Receiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    private AtomicInteger atomicInteger = new AtomicInteger();

    public void receiveMessage(String message) {
        LOGGER.info("Received <" + message + ">");
        atomicInteger.incrementAndGet();
    }

    public int getCount() {
        return atomicInteger.get();
    }
}
