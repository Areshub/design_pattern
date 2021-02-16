package com.lnf.systemio.testreactor;

public class MainThread {
    public static void main(String[] args) {
        // 这里不做关于io  不处理业务
        // 创建IO Thread(一个或者多个)
        // 混杂模式 只有一个线程负责 accpet 每个都会被分配client 进行R/W

        SelectorThreadGroup stg = new SelectorThreadGroup(1);

        // 我应该吧监听的server 注册到某一个selector 上
        stg.bind(9999);
    }
}
