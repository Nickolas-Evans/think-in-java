package com.book.concurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Pair { // Not thread-safe
    private int x, y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pair() {
        this(0, 0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void incrementX() {
        x++;
    }

    public void incrementY() {
        y++;
    }

    public String toString() {
        return "x: " + x + ", y: " + y;
    }

    // Arbitrary invariant -- both variables must be equal:
    public void checkState() {
        if (x != y)
            throw new PairValuesNotEqualException();
    }

    class PairValuesNotEqualException extends RuntimeException {
        public PairValuesNotEqualException() {
            super("Pair values not equal: " + Pair.this); // 还能这么调用外围类示例
        }
    }

}


// Protect a Pair inside a thread-safe class:
abstract class PairManager {
    protected Pair p = new Pair();
    AtomicInteger checkCounter = new AtomicInteger(0);
    List<Pair> storage = Collections.synchronizedList(new ArrayList<>());

     Lock lock = new ReentrantLock();

    public Pair getPair() {
        lock.lock();try{
            // Make a copy to keep the original safe:
            return new Pair(p.getX(), p.getY());
        }finally {
            lock.unlock();
        }

    }

    // Assume this is a time consuming operation
    protected void store(Pair p) {
        storage.add(p);
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException ignore) {
        }
    }

    public abstract void increment();
}

// Synchronize the entire method:
class PairManager1 extends PairManager {
    public synchronized void increment() {
        p.incrementX();
        p.incrementY();
        store(getPair());
    }
}

// Use a critical section:
class PairManager2 extends PairManager {
    public void increment() {
        Pair temp;
        synchronized (this) {
            p.incrementX();
            p.incrementY();
            temp = getPair();
        }
        store(temp);
    }
}

class PairManipulator implements Runnable {
    private PairManager pairManager;

    public PairManipulator(PairManager pairManager) {
        this.pairManager = pairManager;
    }

    public void run() {
        while (true)
            pairManager.increment();
    }

    public String toString() {
        return "Pair: " + pairManager.getPair() + ", " +
                "checkCounter = " + pairManager.checkCounter.get() + ", " +
                "storage size: " + pairManager.storage.size();
    }
}

class PairChecker implements Runnable {
    private PairManager pairManager;

    public PairChecker(PairManager pairManager) {
        this.pairManager = pairManager;
    }

    public void run() {
        while (true) {
            pairManager.checkCounter.incrementAndGet();
            pairManager.getPair().checkState();
        }
    }
}

public class CriticalSection {
    // Test the two different approaches:
    static void
    testApproaches(PairManager pairManager1, PairManager pairManager2) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        PairManipulator pairManipulator1 = null;
        PairManipulator pairManipulator2 = null;
        if (pairManager1 != null) {
            pairManipulator1 = new PairManipulator(pairManager1);
            executorService.execute(pairManipulator1);

            PairChecker pairChecker1 = new PairChecker(pairManager1);
            executorService.execute(pairChecker1);
        }
        if (pairManager2 != null) {
            pairManipulator2= new PairManipulator(pairManager2);
            PairChecker pairChecker2 = new PairChecker(pairManager2);

            executorService.execute(pairManipulator2);
            executorService.execute(pairChecker2);
        }
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted");
        }
        if(pairManager1 != null){
            System.out.println("pairManipulator1: " + pairManipulator1);
        }
        if(pairManager2 != null){
            System.out.println("pairManipulator2: " + pairManipulator2);
        }

        System.exit(0);
    }

    public static void main(String[] args) {
        PairManager pairManager1 = new PairManager1();
        PairManager pairManager2 = new PairManager2();
        testApproaches(pairManager1, pairManager2);
        /*
        pairManipulator1: Pair: x: 52, y: 52, checkCounter = 2,        storage size: 53
        pairManipulator2: Pair: x: 11, y: 11, checkCounter = 30865656, storage size: 11
         */
    }
}