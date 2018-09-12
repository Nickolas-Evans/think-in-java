package com.myself;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Temp {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                int i = System.in.read();
                System.out.println("i: " + i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();

        TimeUnit.SECONDS.sleep(1);

        System.out.println(t.getState());
    }
}
