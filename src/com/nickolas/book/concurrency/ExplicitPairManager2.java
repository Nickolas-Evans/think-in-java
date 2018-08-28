package com.nickolas.book.concurrency;

public class ExplicitPairManager2 extends PairManager {
//    Lock lock = new ReentrantLock();
//
//    @Override
//    public void increment() {
//        lock.lock();
//        Pair pair;
//        try {
//            p.incrementX();
//            p.incrementY();
//            pair = getPair();
//        } finally {
//            lock.unlock();
//        }
//        store(pair);
//    }



    public void increment() {

        lock.lock();
        try {
            p.incrementX();
            p.incrementY();
        } finally {
            lock.unlock();
        }
        store(p);
    }
}
