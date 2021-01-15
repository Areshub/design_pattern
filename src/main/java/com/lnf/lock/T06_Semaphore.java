package com.lnf.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.LockSupport;

public class T06_Semaphore {
    volatile List lists = new ArrayList<>();

    public  void add(Object o ){
        lists.add(o);
    }

    public int size(){return lists.size();}

    static Thread t1=null,t2=null;
    public static void main(String[] args) {
        T06_Semaphore c = new T06_Semaphore();

        Semaphore semaphore = new Semaphore(1);

        t1= new Thread(()->{
            try {
                semaphore.acquire();
                System.out.println("t1 启动");
                for (int i = 0; i < 5; i++) {
                    c.add(new Object());
                    System.out.println("add"+i);
                }
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"t1");

        t2= new Thread(()->{
            try {
                semaphore.acquire();
                System.out.println("t2 启动");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t2");


        t2.start();
        t1.start();
    }
}
