package com.nickolas.book.concurrency;

public class AtomicityTest2 implements Runnable {
    private int i;

    private synchronized void evenIncrement() {
        i++;
        i++;
    }

    public int getI() {
        return i;
    }

    @Override
    public void run() {
        while (true) {
            evenIncrement();
        }
    }

    public static void main(String[] args) {
        AtomicityTest2 test2 = new AtomicityTest2();
        new Thread(test2).start();
        int i;
        while ((i = test2.getI()) < Integer.MAX_VALUE) {
            if (i % 2 != 0) {
                System.out.println("i: " + i);
            }
        }
        System.out.println("overflow");
    }
}
