package com.lnf.lock;

import java.util.ArrayList;
import java.util.List;

public class T04_withoutVolatile {
    List lists = new ArrayList<>();

    public  void add(Object o ){
        lists.add(o);
    }

    public static void main(String[] args) {

    }

}
