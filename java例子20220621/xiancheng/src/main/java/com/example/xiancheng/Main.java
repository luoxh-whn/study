package com.example.xiancheng;

import java.util.Arrays;

public class Main {
 
    public static void f(int... is ) {
        System.out.println(Arrays.toString(is));
    }
     
    public static void g(int[] is) {
        System.out.println(Arrays.toString(is));
    }
     
    public static void main(String... args) {        //可以把String[] 变成String...
        int[] a = new int []{1,2,3,4};
        f(1,2,3,4);
        f(a);
        g(a);
    }
 
}