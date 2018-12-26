package com.book.concurrency;

import static com.book.net.mindview.util.Print.print;

public class LiftOff implements Runnable {

    private static int taskCount = 0;
    int countDown = 10;
    private String id = String.valueOf(taskCount++);

    LiftOff() {
    }

    LiftOff(String id) {
        this.id = id;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while (countDown-- > 0) {
            print(this.status());
            Thread.yield();
        }
    }

    protected String status() {
        return "#" + id + (countDown > 0 ? ("(" + countDown + "), ") : "LiftOff!");
    }
}
