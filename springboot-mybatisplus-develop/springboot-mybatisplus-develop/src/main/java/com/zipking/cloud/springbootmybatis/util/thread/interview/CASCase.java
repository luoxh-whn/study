package com.zipking.cloud.springbootmybatis.util.thread.interview;

public class CASCase {
    public volatile int value;

    public synchronized void add() {
        value++;
    }
}
