package com.lnf.lock;

import java.util.LinkedList;

public class MyContainer<T> {
    final private LinkedList<T> lists = new LinkedList();
    final private int MAX= 10; // 最多十个元素
    private int count =0;

    public synchronized void put(T t){
        while (lists.size()==MAX){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lists.add(t);
        ++count;
        this.notifyAll(); // 通知消费者线程进行消费
    }
    public synchronized T get(){
        T t =null;
        if(lists.size()==0){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        t = lists.removeFirst();
        --count;
        this.notifyAll();
        return t;
    }
}
