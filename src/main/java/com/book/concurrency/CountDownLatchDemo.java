package com.book.concurrency;

import com.google.common.collect.Lists;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.book.net.mindview.util.Print.print;

// Performs some portion of a task:
class TaskPortion implements Runnable {
    private static int counter = 0;
    private static Random rand = new Random(47);
    private final int id = counter++;
    private final CountDownLatch latch;

    public TaskPortion() {
        latch=null;
    }

    TaskPortion(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {
        try {
            doWork();
            latch.countDown();
        } catch (InterruptedException ex) {
            // Acceptable way to exit
        }
    }

    public void doWork() throws InterruptedException {
        TimeUnit.SECONDS.sleep(10);
        print(this + "completed");
    }

    public String toString() {
        return String.format("TaskPortion %1$-3d ", id);
    }

    public int getId() {
        return id;
    }
}

// Waits on the CountDownLatch:
class WaitingTask implements Runnable {
    private static Integer counter = 0;
    private int id;
    private final CountDownLatch latch;

    WaitingTask(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {
        id = counter++;
//        try {
//            latch.await();
//            print("Latch barrier passed for " + this);
//        } catch (InterruptedException ex) {
//            print(this + " interrupted");
//        }
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return String.format("WaitingTask %1$-3d ", id);
    }
}

public class CountDownLatchDemo {
    static final int SIZE = 100;

//    public static void main(String[] args) throws Exception {
//        ExecutorService exec = Executors.newCachedThreadPool();
//        // All must share a single CountDownLatch object:
//        CountDownLatch latch = new CountDownLatch(SIZE);
//        for (int i = 0; i < 10; i++)
//            exec.execute(new WaitingTask(latch));
//        for (int i = 0; i < SIZE; i++)
//            exec.execute(new TaskPortion(latch));
//        print("Launched all tasks");
//        exec.shutdown(); // Quit when all tasks complete
//    }
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        List<WaitingTask> taskList = Lists.newLinkedList();
        WaitingTask task;
        for(int i = 0; i < 5000; i++){
            service.execute(task = new WaitingTask(null));
            taskList.add(task);
        }

        Set<Integer> idSet = new HashSet<>();
        for(WaitingTask t : taskList){
            idSet.add(t.getId());
        }
        System.out.println(idSet.size());
    }
}
