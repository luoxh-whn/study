package com.zipking.cloud.springbootmybatis.util.thread.interview;

public class CoarseSync {
    public static String copyString100Times(String target){
        int i = 0;
        StringBuffer sb = new StringBuffer();
        while (i<100){
            sb.append(target);
        }
        return sb.toString();
    }
}
