package com.book.concurrency;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.book.net.mindview.util.Print.print;
import static com.book.net.mindview.util.Print.printnb;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

class DelayedTask implements Runnable, Delayed {
    private static final List<DelayedTask> sequence = new ArrayList<>();
    private static int counter = 0;
    private final int id = counter++;
    private final int delta;
    private final long trigger;

    private final int delayInMilliseconds;

    DelayedTask(int delayInMilliseconds) {
        this.delayInMilliseconds = delayInMilliseconds;
        delta = delayInMilliseconds;
        trigger = System.nanoTime() + NANOSECONDS.convert(delta, MILLISECONDS);
        sequence.add(this);
    }

    //TODO scs 这个返回固定值也不会影响 DelayQueue ?
    @Override
    public long getDelay(@Nonnull TimeUnit unit) {
        return 0;
//        return unit.convert(trigger - System.nanoTime(), NANOSECONDS);
    }

    //TODO scs 这个返回的值不对就能影响 DelayQueue 的 take 顺序？
    //TODO scs 2019-01-17 21:50 看了 compareTo 的 javadoc
    @Override
    public int compareTo(@Nonnull Delayed arg) {
        DelayedTask that = (DelayedTask) arg;
        return Long.compare(trigger, that.trigger);
    }

    public void run() {
        printnb(this + " ");
    }

    public String toString() {
        return "delta:" + String.format("[%1$-4d]", delta) + ", Task id#" + id;
    }

    private String summary() {
        return "(Task id:" + id + ", delta:" + delta + ")";
    }

    public static class EndSentinel extends DelayedTask {
        private final ExecutorService exec;

        EndSentinel(int delay, ExecutorService e) {
            super(delay);
            exec = e;
        }

        public void run() {
            print();
            print("summary by EndSentinel");
            for (DelayedTask pt : sequence) {
                printnb(pt.summary() + " ");
            }
            print();
//            print(this + "Calling shutdownNow()");
//            exec.shutdownNow();
        }
    }
}

class DelayedTaskConsumer implements Runnable {
    private final DelayQueue<DelayedTask> q;

    DelayedTaskConsumer(DelayQueue<DelayedTask> q) {
        this.q = q;
    }

    public void run() {
        try {
            while (!Thread.interrupted())
                q.take().run(); // Run task with the current thread
        } catch (InterruptedException e) {
            // Acceptable way to exit
        }
        print("Finished DelayedTaskConsumer");
    }
}

public class DelayQueueDemo {
    public static void main(String[] args) {
        Random rand = new Random(47);
        ExecutorService exec = Executors.newCachedThreadPool();
        DelayQueue<DelayedTask> queue = new DelayQueue<>();

        // Fill with tasks that have random delays:
        for (int i = 0; i < 20; i++) {
            queue.put(new DelayedTask(rand.nextInt(5000)));
        }

        // Set the stopping point
        queue.add(new DelayedTask.EndSentinel(5000, exec));
        exec.execute(new DelayedTaskConsumer(queue));

        // Q: 如果不通过 EndSentinel，怎么关闭 ExecutorService？
        // A: ExecutorService javadoc 中含有示例代码，如下
        exec.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!exec.awaitTermination(10, TimeUnit.SECONDS)) {
                exec.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!exec.awaitTermination(10, TimeUnit.SECONDS))
                    System.err.println("exec did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            exec.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

}
