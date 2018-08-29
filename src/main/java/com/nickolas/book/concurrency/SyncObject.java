package com.nickolas.book.concurrency;

public class SyncObject {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public static void main(String[] args) {
        SyncObject syncObject = new SyncObject();
        new Thread(syncObject::f).start();
        syncObject.g();

    }

    void f() {
        synchronized (lock1) {
            for (int i = 0; i < 10; i++) {
                System.out.println("f(), " + i);
                Thread.yield();
            }


        }
    }

    void g() {
        synchronized (lock2) {
            for (int i = 0; i < 10; i++) {
                System.out.println("g(), " + i);
                Thread.yield();
            }
        }
    }
}
