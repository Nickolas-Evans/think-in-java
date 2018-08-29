package com.book.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AtomicityTest implements Runnable {
    private long i = 0;

    private long getValue() {
        return i;
    }

    private synchronized void evenIncrement() {
        i++;
        i++;
    }

    public void run() {
        while (true) {
            evenIncrement();
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        AtomicityTest atomicityTest = new AtomicityTest();
        executorService.execute(atomicityTest);
        executorService.shutdown();

        while (true) {
            long value = atomicityTest.getValue();
            if (value % 2 != 0) {
                //TODO scs 为啥就输出一条？？？？
                System.out.println("value: " + value); // 只输出一条
//                System.out.println(value); // 会输出多条
//                System.exit(0);
            }
        }
    }
}