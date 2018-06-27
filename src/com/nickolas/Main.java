package com.nickolas;

public class Main {

    public static void main(String[] args) {

        // write your code here
        int i;
        i = 1;
        try {
            i = 1 / 0;
        } catch (Exception e) {
            i = i + 1;
            System.out.println("catch: " + i);
        } finally {
            i = i + 2; // 此时 i 的值是否受 catch{} 中的赋值语句所影响？ 会受其影响
            System.out.println("finally776: " + i);
        }
    }
}
