package com.book.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class MutexEvenGenerator extends IntGenerator {
    private int currentIntegerValue = 0;
    private Lock lock = new ReentrantLock();

    @Override
    public int next() {

//        private Lock lock = new ReentrantLock(); // 每个方法都有一把锁？也是傻

        lock.lock();  // block until condition holds
        try {
            // ... method body
            currentIntegerValue++;
            Thread.yield();
            currentIntegerValue++;
            return currentIntegerValue;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {

        EvenChecker.test(new MutexEvenGenerator());
    }
}
