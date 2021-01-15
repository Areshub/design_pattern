package com.lnf.collection;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class TickerSeller {
    static Queue<String> tickers= new ConcurrentLinkedDeque<>();
    static {
        for (int i = 0; i < 10000; i++) {
            tickers.add("票编号"+i);
        }
    }
    static Queue<String> a = new LinkedBlockingDeque();
    public static void main(String[] args) {

     /*   for (int i = 0; i < 10; i++) {
            final int j=i;
            new Thread(()->{
                while (tickers.size()>0){
                    System.out.println(j+"销售了----"+tickers.poll());
                }
            }).start();
        }*/

    }
}
