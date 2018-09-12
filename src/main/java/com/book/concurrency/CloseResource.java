package com.book.concurrency;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.book.net.mindview.util.Print.print;

/**
 * You’ll see from the output that you can interrupt a call to sleep( )
 * (or any call that requires you to catch InterruptedException).
 * However, you cannot interrupt a task that is trying to acquire a
 * synchronized lock or one that is trying to perform I/O. This is a
 * little disconcerting, especially if you’re creating a task that
 * performs I/O, because it means that I/O has the potential of locking
 * your multithreaded program. Especially for Web-based programs,
 * this is a concern.
 *
 * A heavy-handed but sometimes effective solution to this problem is to
 * close the underlying resource on which the task is blocked:
 *
 * TODO scs 所以 ServerSocket 怎么应对？ Servlet 怎么应对线程僵死的？
 */
public class CloseResource {


    public static void main(String[] args) throws Exception {
        new ServerSocket(8080); // 启动本地的 8080 端口

        ExecutorService exec = Executors.newCachedThreadPool();

        InputStream socketInput = new Socket("localhost", 8080).getInputStream();
        exec.execute(new IOBlocked(socketInput));

        exec.execute(new IOBlocked(System.in));

        TimeUnit.MILLISECONDS.sleep(100);

        print("Shutting down all threads");
        exec.shutdownNow();

        /*
            After shutdownNow( ) is called, the delays before calling close()
            on the two input streams emphasize that the tasks unblock once
            the underlying resource is closed. It’s interesting to note that
            the interrupt( ) appears when you are closing the Socket but
            not when closing System.in.

            TODO scs Yes! But why?

            Fortunately, the nio classes introduced in the I/O chapter provide
            for more civilized interruption of I/O. Blocked nio channels
            automatically respond to interrupts:
        */
        TimeUnit.SECONDS.sleep(1);

        print("Closing " + socketInput.getClass().getName());
        socketInput.close(); // Releases blocked thread

        // 所以被 blocked 的 Thread 还不会被自动清理？
        // 不会啊，等待 io 而 blocked 的线程 当然不会被自动清理，不然 io 来了，谁去处理？
        TimeUnit.SECONDS.sleep(1);

        print("Closing " + System.in.getClass().getName());
        System.in.close(); // Releases blocked thread
    }
}
