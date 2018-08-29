package com.book.concurrency;

import java.util.concurrent.TimeUnit;

/**
 * Using a named inner class:
 *
 * InnerThread1 creates a named inner class that extends Thread,
 * and makes an instance of this inner class inside the constructor.
 * This makes sense if the inner class has special capabilities (new methods)
 * that you need to access in other methods.
 * However, most of the time the reason for creating a thread is only to use the Thread capabilities,
 * so it’s not necessary to create a named inner class.
 */
class InnerThread1 {
    private int countDown = 5;
    private final Inner inner;

    private class Inner extends Thread {
        Inner(String name) {
            super(name);
            start();
        }

        public void run() {
            try {
                while (true) {
                    System.out.println(this);
                    if (--countDown == 0) return;
                    sleep(10);
                }
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
        }

        public String toString() {
            return getName() + ": " + countDown;
        }
    }

    public InnerThread1(String name) {
        inner = new Inner(name);
    }
}



/**
 * Using an anonymous inner class
 *
 * InnerThread2 shows the alternative: An anonymous inner subclass of Thread is created inside the constructor
 * and is up-cast to a Thread reference t. If other methods of the class need to access t,
 * they can do so through the Thread interface, and they don’t need to know the exact type of the object.
 */
class InnerThread2 {
    private int countDown = 5;
    private final Thread t;

    public InnerThread2(String name) {

        t = new Thread(name) {
            public void run() {
                try {
                    while (true) {
                        System.out.println(this);
                        if (--countDown == 0) return;
                        sleep(10);
                    }
                } catch (InterruptedException e) {
                    System.out.println("sleep() interrupted");
                }
            }

            public String toString() {
                return getName() + ": " + countDown;
            }
        };

        t.start();

    }

}

/**
 * Using a named Runnable implementation:
 *
 * The third and fourth classes in the example repeat the first two classes,
 * but they use the Runnable interface rather than the Thread class.
 */
class InnerRunnable1 {
    private int countDown = 5;
    private final Inner inner;

    private class Inner implements Runnable {
        final Thread t;

        Inner(String name) {
            t = new Thread(this, name);
            t.start();
        }

        public void run() {
            try {
                while (true) {
                    System.out.println(this);
                    if (--countDown == 0) return;
                    TimeUnit.MILLISECONDS.sleep(10);
                }
            } catch (InterruptedException e) {
                System.out.println("sleep() interrupted");
            }
        }

        public String toString() {
            return t.getName() + ": " + countDown;
        }
    }

    InnerRunnable1(String name) {
        inner = new Inner(name);
    }
}

// Using an anonymous Runnable implementation:
class InnerRunnable2 {
    private int countDown = 5;
    private final Thread t;

    InnerRunnable2(String name) {
        t = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        System.out.println(this);
                        if (--countDown == 0) return;
                        TimeUnit.MILLISECONDS.sleep(10);
                    }
                } catch (InterruptedException e) {
                    System.out.println("sleep() interrupted");
                }
            }

            public String toString() {
                return Thread.currentThread().getName() +
                        ": " + countDown;
            }
        }, name);
        t.start();
    }
}

/**
 * A separate method to run some code as a task:
 *
 * The ThreadMethod class shows the creation of a thread inside a method.
 * You call the method when you’re ready to run the thread,
 * -                                                 -
 * -                                                 -
 * - and the method returns after the thread begins. -
 * -                                                 -
 * -                                                 -
 * If the thread is only performing an auxiliary operation rather than being fundamental to the class,
 * this is probably a more useful and appropriate approach than starting a thread inside the constructor of the class.
 */
class ThreadMethod {
    private int countDown = 5;
    private Thread t;
    private final String name;

    public ThreadMethod(String name) {
        this.name = name;
    }

    public void runTask() {
        if (t == null) {
            t = new Thread(name) {
                public void run() {
                    try {
                        while (true) {
                            System.out.println(this);
                            if (--countDown == 0) return;
                            sleep(10);
                        }
                    } catch (InterruptedException e) {
                        System.out.println("sleep() interrupted");
                    }
                }

                public String toString() {
                    return getName() + ": " + countDown;
                }
            };
            t.start();
        }
    }
}

public class ThreadVariations {
    public static void main(String[] args) {
        new InnerThread1("InnerThread1");
        new InnerThread2("InnerThread2");
        new InnerRunnable1("InnerRunnable1");
        new InnerRunnable2("InnerRunnable2");
        new ThreadMethod("ThreadMethod").runTask();
    }
}