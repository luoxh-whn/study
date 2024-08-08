package test;

import java.util.ArrayList;
import java.util.List;

public class MyThread4 {


    public static void main(String[] args) throws InterruptedException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            //��̬���߳����Ƽ��뼯��
            new Thread(() -> {
                synchronized (list) {
                    list.add(Thread.currentThread().getName());
                }
            }).start();
        }
        Thread.sleep(5000);
        System.out.println(list.size());
    }
}