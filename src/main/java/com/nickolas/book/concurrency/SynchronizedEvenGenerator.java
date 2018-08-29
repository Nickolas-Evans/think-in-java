package com.nickolas.book.concurrency;

public class SynchronizedEvenGenerator extends IntGenerator {
    private int i;

    @Override
    public synchronized int next() {
        i++;
        Thread.yield();
        i++;
        return i;
    }

    public static void main(String[] args) {
        EvenChecker.test(new SynchronizedEvenGenerator());
    }
}
