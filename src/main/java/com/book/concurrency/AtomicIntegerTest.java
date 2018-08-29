package com.book.concurrency;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {
    private AtomicInteger i = new AtomicInteger();

    private void evenIncrement() {
        i.addAndGet(2);
    }

    public int getI() {
        return i.get();
    }

    public static void main(String[] args) {
        AtomicIntegerTest atomicIntegerTest = new AtomicIntegerTest();
        new Thread(() -> {
            while (true) {
                atomicIntegerTest.evenIncrement();
            }
        }).start();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("system out. i: " + atomicIntegerTest.getI());
                System.exit(-1);
            }
        }, 10000);

        while (true) {
            int i = atomicIntegerTest.getI();
            if (i % 2 != 0) {
                System.out.println("not even: " + i);
            }
        }
    }

}
