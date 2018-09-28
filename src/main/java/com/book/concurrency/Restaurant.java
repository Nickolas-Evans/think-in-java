package com.book.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.book.net.mindview.util.Print.print;
import static com.book.net.mindview.util.Print.printnb;

class Meal {
    private final int orderNum;

    public Meal(int orderNum) {
        this.orderNum = orderNum;
    }

    public String toString() {
        return "Meal " + orderNum;
    }
}

class WaitPerson implements Runnable {
    private Restaurant restaurant;

    public WaitPerson(Restaurant r) {
        restaurant = r;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) { // 服务员等待的时候，锁加在自己身上有啥用？让自己能够 wait
                    // 但不能保证自己被唤醒的同时，餐被其他服务员取走啊，所以锁应该加在餐上？但是餐会被销毁并重建 而不是 final 啊
                    // 所以锁应该加在传菜窗口上
                    while (restaurant.meal == null)
                        wait(); // ... for the chef to produce a meal
                }
                Thread.yield(); // 从线程中唤醒后没有检测条件，导致如果服务员多的话，就抢餐了
                print("Waitperson got " + restaurant.meal);
                synchronized (restaurant.chef) {
                    restaurant.meal = null;
                    restaurant.chef.notifyAll(); // Ready for another
                }
            }
        } catch (InterruptedException e) {
            print("WaitPerson interrupted");
        }
    }
}

class Chef implements Runnable {
    private Restaurant restaurant;
    private int count = 0;

    public Chef(Restaurant r) {
        restaurant = r;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    while (restaurant.meal != null)
                        wait(); // ... for the meal to be taken
                }
                if (++count == 10) {
                    print("Out of food, closing");
                    restaurant.exec.shutdownNow();
//                    return;
                }
                synchronized (this) {
                    printnb("Order up! 1");
                    printnb("Order up! 2");
                }
                synchronized (restaurant.waitPerson) { // SCS 如果餐厅有多个服务员和单个厨师呢？
                    restaurant.meal = new Meal(count); // SCS 如果餐厅有多个服务员和多个厨师呢？
                    restaurant.waitPerson.notifyAll();
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (InterruptedException e) {
            print("Chef interrupted");
        }
    }
}

public class Restaurant {
    Meal meal;
    ExecutorService exec;
    final WaitPerson waitPerson;
    final Chef chef;

    public Restaurant() {
        exec = Executors.newCachedThreadPool();
        waitPerson = new WaitPerson(this);
        chef = new Chef(this);
    }

    private void open() {
        exec.execute(chef);
        exec.execute(waitPerson);
//        exec.execute(waitPerson);
//        exec.execute(waitPerson);
//        exec.execute(waitPerson);
        exec.shutdown();
    }

    public static void main(String[] args) {
        new Restaurant().open();
    }
}