package com.zipking.cloud.springbootmybatis.util.abstracts;

public class Test3 extends Test2{

    @Override
    public void B() {
        System.out.println("this is B");
    }

    public static void main(String[] args) {
        Test3 test3 = new Test3();
        test3.A();
        test3.B();
    }
}
