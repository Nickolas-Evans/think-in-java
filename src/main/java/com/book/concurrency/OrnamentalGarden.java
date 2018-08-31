package com.book.concurrency;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A single Count object keeps the master count of garden visitors, and is stored as a static
 * field in the Entrance class.
 * <p>
 * Count.increment( ) and Count.value( ) are synchronized to control access to the count field.
 * The increment( ) method uses a Random object to cause a yield( ) roughly half the time,
 * in between fetching count into temp and incrementing and storing temp back into count.
 * If you comment out the synchronized keyword on increment( ), the program breaks because
 * multiple tasks will be accessing and modifying count simultaneously (the yield( ) causes
 * the problem to happen more quickly).
 */
class Count {
    private int count = 0;
    private Random rand = new Random(47);

    /**
     * Keep in mind that Count.increment( ) exaggerates the potential for failure by using temp and yield( ).
     *
     * Just as in the example above, there are likely to be hidden problems that haven’t occurred to you,
     * so be exceptionally diligent when reviewing concurrent code.
     */
    // Remove the synchronized keyword to see counting fail:
    public synchronized int increment() {
        int temp = count;
        if (rand.nextBoolean()) // Yield half the time
            Thread.yield();
        return (count = ++temp);
    }

    public int value() { // 是否可以不用做同步？ 可以的
        return count;
    }
}

class Entrance implements Runnable {
    private static Count count = new Count();
    private static List<WeakReference<Entrance>> entrances =
            new ArrayList<>();

    /**
     * Each Entrance task keeps a local value number containing the number of visitors that have
     * passed through that particular entrance. This provides a double check against the count
     * object to make sure that the proper number of visitors is being recorded.
     */
    private int number = 0;

    // Doesn’t need synchronization to read:
    private final int id;
    private static volatile boolean canceled = false;

    // Atomic operation on a volatile field:
    public static void cancel() {
        canceled = true;
    }

    public Entrance(int id) {
        this.id = id;
        // Keep this task in a list. Also prevents
        // garbage collection of dead tasks:
        entrances.add(new WeakReference<>(this));
    }

    /**
     * Entrance.run() simply increments number and the count object and sleeps for 100 milliseconds.
     */
    public void run() {
        while (!canceled) {
//            synchronized (this) { // 有必要？
            // 似乎没有
            ++number;
//            }
            System.out.println(this + " Total: " + count.increment());
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("sleep interrupted");
            }
        }
        System.out.println("Stopping " + this);
    }

    public int getValue() { // 有必要同步？
        // 没有
        return number;
    }

    public String toString() {
        return "Entrance " + id + ": " + getValue();
    }

    public static int getTotalCount() {
        return count.value();
    }

    public static int sumEntrances() {
        int sum = 0;
        for (WeakReference<Entrance> entrance : entrances)
            sum += entrance.get().getValue(); //果然炸了
        return sum;
    }
}

public class OrnamentalGarden {

    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++)
            exec.execute(new Entrance(i));

        /*
         * After 10 seconds, main( ) sends the static cancel( ) message to Entrance,
         * then calls shutdown( ) for the exec object, and then calls awaitTermination()
         * on exec. ExecutorService.awaitTermination( ) waits for each task to complete,
         * and if they all complete before the timeout value, it returns true, otherwise
         * it returns false to indicate that not all tasks have completed. Although this
         * causes each task to exit its run( ) method and therefore terminate as a task,
         * the Entrance objects are still valid because, in the constructor, each Entrance
         * object is stored in a static List<Entrance> called entrances. Thus,
         * sumEntrances() is still working with valid Entrance objects.
         */
        //TODO scs 还真没法验证 Entrances 是否被回收
        // Run for a while, then stop and collect the data:
        TimeUnit.SECONDS.sleep(3);
        Entrance.cancel();
        exec.shutdown();

        /*
         * This program goes to quite a bit of extra trouble to shut everything down
         * in a stable fashion. Part of the reason for this is to show just how careful
         * you must be when terminating a multi-threaded program, and part of the reason
         * is to demonstrate the value of interrupt( ), which you will learn about shortly.
         */
        if (!exec.awaitTermination(250, TimeUnit.MILLISECONDS)) {
            System.out.println("Some tasks were not terminated!");
        }
        System.gc();
        TimeUnit.SECONDS.sleep(10);
        System.out.println("Total: " + Entrance.getTotalCount());
        System.out.println("Sum of Entrances: " + Entrance.sumEntrances());
    }
}