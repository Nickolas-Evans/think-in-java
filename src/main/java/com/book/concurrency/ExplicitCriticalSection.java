package com.book.concurrency;

public class ExplicitCriticalSection {

    public static void main(String[] args) {
        PairManager pairManager1 = new ExplicitPairManager1();
        PairManager pairManager2 = new ExplicitPairManager2();
        CriticalSection.testApproaches(pairManager1, pairManager2);
        // 为什么 increment 方法 不加 sync 就线程不安全？
        // 应为 getPair 与 increment 不是用的同一个锁，则 increment 的同时可以进行 getPair
    }

}
