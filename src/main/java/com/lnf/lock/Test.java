package com.lnf.lock;


import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;


public class Test {
    public static void main(String[] args) throws InterruptedException {

        Test waitDemo = new Test();

        // 启动新线程，防止主线程被休眠
        new Thread(() -> {
            try {
                waitDemo.doWait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                waitDemo.doNotify();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


  /*      PriorityQueue<String> priorityQueue = new PriorityQueue();

        priorityQueue.add("d");
        priorityQueue.add("y");
        priorityQueue.add("r");
        priorityQueue.add("a");
        priorityQueue.add("a");
        while (true){
            if(priorityQueue.isEmpty()) break;
            System.out.println(priorityQueue.poll());
        }
*/


/*
        BlockingQueue<String> str = new SynchronousQueue<>();

        new Thread(() -> {
            try {
                str.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        str.put("aaaa");
        System.out.println(str.size());
*/

     /*   LinkedTransferQueue<String> strs = new LinkedTransferQueue<>();


        new Thread(()->{
            try {
                System.out.println(strs.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()->{
            try {
                System.out.println(strs.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()->{
            try {
                System.out.println(strs.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


        strs.transfer("aa");
        strs.transfer("bb");
        strs.transfer("cc");*/

    }



    private static Object locker = new Object();


    /**
     * 执行 wait()
     */
    private void doWait() throws InterruptedException {
        synchronized (locker) {
            System.out.println("wait start.");
            locker.wait();
            System.out.println("wait end.");
        }
    }

    /**
     * 执行 notify()
     */
    private void doNotify() {
        synchronized (locker) {
            System.out.println("notify start.");
            locker.notify();
            System.out.println("notify end.");
        }
    }
}