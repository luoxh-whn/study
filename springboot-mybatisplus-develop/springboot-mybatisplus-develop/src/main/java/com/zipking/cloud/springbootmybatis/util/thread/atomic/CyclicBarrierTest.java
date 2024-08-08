package com.zipking.cloud.springbootmybatis.util.thread.atomic;

import java.util.concurrent.CyclicBarrier;

/**
 * CountDownLatch的计数器只能使用一次，
 * 而CyclicBarrier的计数器可以使用reset()方法重置。
 * 所以CyclicBarrier能处理更为复杂的业务场景
 * */
public class CyclicBarrierTest {

    static CyclicBarrier c = new CyclicBarrier(2);
    public static void main(String[] args) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    c.await();
                } catch (Exception e) {
                }
                System.out.println(1);
            }
        }).start();

        try{
            c.await();
        }catch (Exception e){
        }

        System.out.println(2);
    }
}
