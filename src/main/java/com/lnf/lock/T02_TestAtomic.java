package com.lnf.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

public class T02_TestAtomic {
    private static ReentrantLock lock = new ReentrantLock();
    static Long count2 =0l;
    static AtomicLong count1= new AtomicLong(0l);
    static LongAdder count3 =new LongAdder();
    public static void main(String[] args) {
        Thread[] threads = new Thread[1000];
        // 倒计时锁
        CountDownLatch latch = new CountDownLatch(threads.length);
        for (int j= 0; j < threads.length; j++) {
            threads[j]=new Thread(()->{
                count1.incrementAndGet();
                latch.countDown();
            });
        }
        long l = System.currentTimeMillis();
        for (Thread x: threads) x.start();
      /*  for (Thread x: threads) {
            try {
                x.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long l1 = System.currentTimeMillis();
        System.out.println("Atomic:"+count1.get()+"time"+(l1-l));
        // ----------------------------------------------------------------
/*        Object lock = new Object();
        for (int j= 0; j < threads.length; j++) {
            threads[j]=new Thread(()->{
                for (int i = 0; i < 10000; i++) {
                   synchronized (lock){
                       count2++;
                   }
                }
            });
        }

        long l = System.currentTimeMillis();
        for (Thread x: threads) {
            x.start();
            System.out.println(x.getName());
        }
        for (Thread x: threads) {
            try {
                x.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long l1 = System.currentTimeMillis();
        System.out.println("Sync:"+count2+"time"+(l1-l));*/
        // ----------------------------------------------------------------
/*
        for (int j= 0; j < threads.length; j++) {
            threads[j]=new Thread(()->{
                for (int i = 0; i < 10000; i++) {
                    count3.increment();
                }
            });
        }
        long l = System.currentTimeMillis();
        for (Thread x: threads) x.start();
        for (Thread x: threads) {
            try {
                x.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long l1 = System.currentTimeMillis();
        System.out.println("LongAdder:"+count3+"time"+(l1-l));
*/

    }
}
