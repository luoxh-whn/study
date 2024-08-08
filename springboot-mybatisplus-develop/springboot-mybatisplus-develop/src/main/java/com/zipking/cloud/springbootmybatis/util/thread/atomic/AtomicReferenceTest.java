package com.zipking.cloud.springbootmybatis.util.thread.atomic;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {
    public static AtomicReference<User> atomicUserRef = new
            AtomicReference<User>();
    public static void main(String[] args) {
        User user = new User("conan",15);
        atomicUserRef.set(user);
        User updateUser = new User("Shinichi",17);
        atomicUserRef.compareAndSet(user,updateUser);
        System.out.println(user.getName());
        System.out.println(user.getOld());
        System.out.println(updateUser.getName());
        System.out.println(updateUser.getOld());
        System.out.println(atomicUserRef.get().getName());
        System.out.println(atomicUserRef.get().getOld());
    }
    static class User {
        private String name;
        private int old;
        public User(String name, int old) {
            this.name = name;
            this.old = old;
        }
        public String getName() {
            return name;
        }
        public int getOld() {
            return old;
        } }
}
