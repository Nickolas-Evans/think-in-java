package com.myself.concurrency.waxomatic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.book.net.mindview.util.Print.print;

class Car {
    private boolean waxOn = false;

    synchronized void waxed() {
        waxOn = true; // Ready to buff
        notifyAll();
    }

    synchronized void buffed() {
        waxOn = false; // Ready for another coat of wax
        notifyAll();
    }

    synchronized void waitForWaxing()
            throws InterruptedException {
        while (!waxOn)
            wait();
    }

    synchronized void waitForBuffing()
            throws InterruptedException {
        while (waxOn)
            wait();
    }
}

class WaxOn implements Runnable {
    private final Car car;

    WaxOn(Car c) {
        car = c;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println("Wax On! ");
                TimeUnit.MILLISECONDS.sleep(200);
                car.waxed();
                car.waitForBuffing();
            }
        } catch (InterruptedException e) {
            print("Exiting via interrupt");
        }
        print("Ending Wax On task");
    }

}

class WaxOff implements Runnable {
    private final Car car;

    WaxOff(Car c) {
        car = c;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                car.waitForWaxing();
                System.out.println("Wax Off! ");
                TimeUnit.MILLISECONDS.sleep(200);
                car.buffed();
            }
        } catch (InterruptedException e) {
            print("Exiting via interrupt");
        }
        print("Ending Wax Off task");
    }


}

public class WaxOMatic {
    public static void main(String[] args) throws Exception {
        Car car = new Car();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new WaxOff(car));
        exec.execute(new WaxOn(car));
        TimeUnit.SECONDS.sleep(5); // Run for a while...
        exec.shutdownNow(); // Interrupt all tasks
    }
}
