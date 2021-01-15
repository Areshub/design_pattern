package com.lnf.collection;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class T01_00_Exchanger {
    private volatile static Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) {
        char[] char1= "1234567".toCharArray();
        char[] char2 ="ABCDEFG".toCharArray();

        new Thread(()->{
            for (int i = 0; i < char1.length; i++) {
                System.out.println(char1[i]);
                try {
                    exchanger.exchange("T1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(()->{
            for (int i = 0; i < char2.length; i++) {
                try {
                     exchanger.exchange("T2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(char2[i]);
            }
        }).start();
    }
}
