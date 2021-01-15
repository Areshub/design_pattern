package com.lnf.collection;



import org.apache.coyote.Constants;

import java.util.Hashtable;
import java.util.UUID;

public class T01_TestHashTable {

    static Hashtable<UUID,UUID> m = new Hashtable<>();

    static int count =1000000;

    static UUID[] keys = new UUID[count];

    static UUID[] value = new UUID[count];

    static final  int THREAD_COUNT = 100;

    static {
        for (int i = 0; i < count; i++) {
            keys[i] = UUID.randomUUID();
            value[i] = UUID.randomUUID();
        }
    }

    static class MyThread extends Thread{

        int start;

        int gap = count/THREAD_COUNT;

        @Override
        public void run(){
            for (int i = start; i <start+gap ; i++) {
                m.put(keys[i],value[i]);
            }
        }
    }

}
