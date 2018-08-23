package com.nickolas.book.concurrency;

public class EvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;

    public int next() {
        ++currentEvenValue; // Danger point here!
        ++currentEvenValue;
        return currentEvenValue;
    }

    // scs 为什么程序死了？
    // 因为并发问题没复现 QAQ
    public static void main(String[] args) {
        EvenChecker.test(new EvenGenerator());
    }
}