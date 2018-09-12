package com.myself;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * When you run the program, you’ll see that, unlike an I/O call,
 * interrupt( ) can break out of a call that’s blocked by a mutex.19
 * <p>
 * Thread.interrupt() 不能中断 IO ？ 只是设置中断状态
 *
 * <p>
 * 感觉更准确地说是无法优雅地中断被 IO 阻塞的线程
 */
public class InterruptIO {

    public static void main(String[] args) throws InterruptedException, IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

        Thread t = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    try (
                            Socket clientSocket = serverSocket.accept();
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(clientSocket.getInputStream()));
                    ) {
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            System.out.println("inputLine: " + inputLine);
                        }
                    } catch (IOException e) {
                        System.out.println("Exception caught when trying to listen on port 8080 or listening for a connection");
                        System.out.println(e.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    System.out.println("sleeping !!!!!");
                    throw new RuntimeException(e);
                }
            }
            System.out.println("interrupted");
        });


        sendToSocket("before start");
        t.start();
        sendToSocket("after start");

        TimeUnit.SECONDS.sleep(1);

        sendToSocket("before interrupt1");
        t.interrupt();  // 只是设置 interrupt 状态，并不会让线程抛出 InterruptedException
        sendToSocket("after interrupt1");

        sendToSocket("before interrupt2");
        t.interrupt();
        sendToSocket("after interrupt2");

        System.exit(0);
    }

    private static void sendToSocket(String message) {
        try (
                Socket kkSocket = new Socket("localhost", 8080);
                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
        ) {
            out.println(message);
            out.println();
            out.flush();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: localhost");
            System.exit(1);
        }
    }
}
