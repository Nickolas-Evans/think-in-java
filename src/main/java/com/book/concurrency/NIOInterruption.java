package com.book.concurrency;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.book.net.mindview.util.Print.print;

class NIOBlocked implements Runnable {
    private final SocketChannel sc;

    NIOBlocked(SocketChannel sc) {
        this.sc = sc;
    }

    @Override
    public void run() {
        try {
            print("Waiting for read() in " + this);
            sc.read(ByteBuffer.allocate(1));
        } catch (ClosedByInterruptException e) {
            print("ClosedByInterruptException");
        } catch (AsynchronousCloseException e) {
            print("AsynchronousCloseException");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        print("Exiting NIOBlocked.run() " + this + ", socket channel class: " + sc.getClass().getName());
    }

}

/**
 * As shown, you can also close the underlying channel to release the block,
 * although this should rarely be necessary. Note that using execute( ) to
 * start both tasks and calling e.shutdownNow( ) will easily terminate
 * everything; capturing the Future in the example above was only necessary
 * to send the interrupt to one thread and not the other.
 */
public class NIOInterruption {

    public static void main(String[] args) throws Exception {
        new ServerSocket(8080);

        ExecutorService executorService = Executors.newCachedThreadPool();

        InetSocketAddress isa = new InetSocketAddress("localhost", 8080);
        SocketChannel sc1 = SocketChannel.open(isa);
        SocketChannel sc2 = SocketChannel.open(isa);

        System.out.println("sc1.isOpen()?: " + sc1.isOpen());
        System.out.println("sc1.isConnected()?: " + sc1.isConnected());

        System.out.println("sc2.isOpen()?: " + sc2.isOpen());
        System.out.println("sc2.isConnected()?: " + sc2.isConnected());

        Future<?> f = executorService.submit(new NIOBlocked(sc1));
        executorService.execute(new NIOBlocked(sc2));
        executorService.shutdown();

        TimeUnit.SECONDS.sleep(1);
        // Produce an interrupt via cancel:
        f.cancel(true);

        TimeUnit.SECONDS.sleep(1);
        // Release the block by closing the channel:
        sc2.close();
    }
}