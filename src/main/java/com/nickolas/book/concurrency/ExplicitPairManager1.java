package com.nickolas.book.concurrency;

public class ExplicitPairManager1 extends PairManager {
//    private Lock lock = new ReentrantLock();
//
//    @Override
//    public void increment() {
//        lock.lock();
//        try {
//            p.incrementX();
//            p.incrementY();
//            store(getPair());
//        } finally {
//            lock.unlock();
//        }
//    }


    public void increment() {
        lock.lock();
        try {
            p.incrementX();
            p.incrementY();
            store(p);
        } finally {
            lock.unlock();
        }
    }

}
