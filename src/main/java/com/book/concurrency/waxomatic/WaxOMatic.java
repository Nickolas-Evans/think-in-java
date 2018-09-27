package com.book.concurrency.waxomatic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.book.net.mindview.util.Print.print;
import static com.book.net.mindview.util.Print.printnb;

class Car {
    private boolean waxOn = false;

    public synchronized void changeStatusToWaxOn() {
        waxOn = true; // Ready to buff

    }

    public synchronized void changeStatusToWaxOff() {
        waxOn = false; // Ready for another coat of wax
    }

    public synchronized void waitForWaxing()
            throws InterruptedException {
        while (waxOn == false)
            wait();
    }

    public synchronized void waitForBuffing()
            throws InterruptedException {
        while (waxOn == true)
            wait();
    }

    public boolean isWaxOn() {
        return waxOn;
    }

    public void setWaxOn(boolean waxOn) {
        this.waxOn = waxOn;
    }
}

class WaxOn implements Runnable {
    private final Car car;

    public WaxOn(Car c) {
        car = c;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (car) {
                    if (!car.isWaxOn()) {
                        printnb("wax on start");
                        TimeUnit.MILLISECONDS.sleep(200);
                        car.setWaxOn(true);
                        printnb("wax on over");
                        car.notifyAll();
                        car.wait();
                    }
                }

            }
        } catch (InterruptedException e) {
            print("wax on - Exiting via interrupt");
        }
        print("wax on - Ending Wax On task");
    }
}

class WaxOff implements Runnable {

    private final Car car;

    public WaxOff(Car c) {
        car = c;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (car) {
                    if (car.isWaxOn()) {
                        printnb("wax off start");
                        TimeUnit.MILLISECONDS.sleep(200);
                        car.setWaxOn(false);
                        printnb("wax off over");
                        car.notifyAll();
                        car.wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            print("wax off - Exiting via interrupt");
        }
        print("wax off - Ending Wax Off task");
    }
}

public class WaxOMatic {

    public static void main(String[] args) throws Exception {
        Car car = new Car();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new WaxOn(car));
        exec.execute(new WaxOn(car));
        exec.execute(new WaxOn(car));
        exec.execute(new WaxOn(car));
        exec.execute(new WaxOn(car));
        exec.execute(new WaxOn(car));
        exec.execute(new WaxOn(car));
        exec.execute(new WaxOff(car));
        exec.execute(new WaxOff(car));
        exec.execute(new WaxOff(car));
        exec.execute(new WaxOff(car));
        exec.execute(new WaxOff(car));
        exec.execute(new WaxOff(car));
        exec.execute(new WaxOff(car));
        exec.execute(new WaxOff(car));
        exec.execute(new WaxOn(car));
        exec.execute(new WaxOn(car));
        exec.execute(new WaxOn(car));
        exec.execute(new WaxOn(car));
        exec.execute(new WaxOn(car));
        exec.execute(new WaxOn(car));

        TimeUnit.SECONDS.sleep(10); // Run for a while...
        exec.shutdownNow(); // Interrupt all tasks
    }
}