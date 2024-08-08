package com.zipking.cloud.springbootmybatis.util.eatwhat;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Eat {

    private static List<String> eats = new ArrayList<>();

    public static void getLikeEats() {
        eats.add("黄焖鸡米饭");
        eats.add("酸菜鱼");
        eats.add("麻辣烫");
    }

    public static void getUnLikeEats() {
        eats.add("开味花甲");
        eats.add("无名缘米粉");
        eats.add("牛杂汤");
        eats.add("牛杂盖饭");
        eats.add("小碗菜");
        eats.add("陕西面馆-盖浇面");
        eats.add("陕西面馆-拌面");
        eats.add("山西面馆");
        eats.add("日韩-汤面");
        eats.add("日韩-拌饭");
        eats.add("日韩-盖浇饭");
        eats.add("自称重饭");
    }

    public static void main(String[] args) throws InterruptedException {
        final HashMap<String, String> map = new HashMap<String, String>(2);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            map.put(UUID.randomUUID().toString(), "");
                        }
                    }, "ftf" + i).start();
                }
            }
        }, "ftf");
        t.start();
        t.join();
        Map<String,String> uy = new ConcurrentHashMap<>();
        getLikeEats();
        Random rn = new Random();
        int num = rn.nextInt(eats.size());
        System.out.println(eats.get(num));
    }
}
