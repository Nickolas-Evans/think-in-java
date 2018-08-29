package com.nickolas.exercise.exercise6;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * Exercise 6: (2) Create a task that sleeps for a random amount of time between 1 and 10 seconds,
 * then displays its sleep time and exits. Create and run a quantity (given on the command line) of these tasks.
 */
public class Exercise6 {

    private static final Random random  = new Random(10);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            executorService.execute(() -> {
                long randomAmountOfSleepingTime = random.nextLong() % 10 + 1;
                try {
                    TimeUnit.MILLISECONDS.sleep(randomAmountOfSleepingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("sleeping time: " + randomAmountOfSleepingTime);
                }
            });
        }
        executorService.shutdown();
    }
}


