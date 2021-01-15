package com.lnf.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class T05_LockSupportSleep {
    volatile List lists = new ArrayList<>();

    public  void add(Object o ){
        lists.add(o);
    }

    public int size(){return lists.size();}

    static Thread t1=null,t2=null;
    public static void main(String[] args) {
        T05_LockSupportSleep c = new T05_LockSupportSleep();
        t1= new Thread(()->{
            System.out.println("t1 启动");
            for (int i = 0; i < 10; i++) {
                c.add(new Object());
                System.out.println("add"+i);
                if(c.size()==5){
                    LockSupport.unpark(t2);
                    LockSupport.park();
                }
            }
        },"t1");

        t2= new Thread(()->{
            System.out.println("t2 启动");
    /*        if(c.size()!=5){
                LockSupport.park();
            }*/
            LockSupport.park();
            System.out.println("t2 结束");
            LockSupport.unpark(t1);
        },"t2");
        t2.start();
        t1.start();
    }

}
