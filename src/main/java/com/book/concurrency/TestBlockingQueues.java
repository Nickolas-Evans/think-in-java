package com.book.concurrency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import static com.book.net.mindview.util.Print.print;

class LiftOffRunner implements Runnable {
    private final BlockingQueue<LiftOff> rockets;

    LiftOffRunner(BlockingQueue<LiftOff> queue) {
        rockets = queue;
    }

    void add(LiftOff lo) {
        try {
            rockets.put(lo);
        } catch (InterruptedException e) {
            print("Interrupted during put()");
            Thread.currentThread().interrupt();
        }
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                LiftOff rocket = rockets.take();
                rocket.run(); // Use this thread
            }
        } catch (InterruptedException e) {
            print("Waking from take()");
            Thread.currentThread().interrupt();
        }
        print("Exiting LiftOffRunner");
    }
}

public class TestBlockingQueues {

    private static void test(String msg, BlockingQueue<LiftOff> queue) {
        print("start " + msg + " test");

        LiftOffRunner runner = new LiftOffRunner(queue);
        Thread t = new Thread(runner);
        t.start();

        for (int i = 0; i < 5; i++) {
            runner.add(new LiftOff(msg + i));
        }
        t.interrupt();
        print("Finished " + msg + " test");
    }

    public static void main(String[] args) {
        test("LinkedBlockingQueue", new LinkedBlockingQueue<>()); // Unlimited size
        test("ArrayBlockingQueue", new ArrayBlockingQueue<>(3)); // Fixed size
        test("SynchronousQueue", new SynchronousQueue<>()); // Size of 1
    }
}