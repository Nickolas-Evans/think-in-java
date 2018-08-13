package com.nickolas.exercise.forvolatile;

import java.util.Calendar;

/**
 * Example 8.3.1.4-1. volatile Fields
 * <p>
 * If, in the following example, one thread repeatedly calls the method one
 * (but no more than Integer.MAX_VALUE times in all),
 * and another thread repeatedly calls the method two:
 *
 * @see <a href="https://docs.oracle.com/javase/specs/jls/se10/html/jls-8.html#jls-8.3.1.4">JLS - 8.3.1.4. volatile Fields</>
 */
public class Test {
    private static int i = 0, j = 0;

    private static synchronized void one() {
        i++;
        j++;
    }

    private static synchronized void two() {
        System.out.println("i=" + i + " j=" + j);
    }

    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            while (true) Test.one();
        });
        t.setDaemon(true);
        t.start();

        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.SECOND, 30);

        do {
            Test.two();
        }
        while (Calendar.getInstance().before(endTime));
    }
}
