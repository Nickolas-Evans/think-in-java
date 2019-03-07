package com.myself.laboratory.mycomparable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class MyComparableObject implements Comparable<MyComparableObject> {

    private int priority;

    public MyComparableObject(int i) {
        this.priority = i;
    }

    public static void main(String[] args) {
        List<MyComparableObject> myComparableObjectList = new ArrayList<>();
        myComparableObjectList.add(new MyComparableObject(4));
        myComparableObjectList.add(new MyComparableObject(4));
        myComparableObjectList.add(new MyComparableObject(4));
        myComparableObjectList.add(new MyComparableObject(4));
        myComparableObjectList.add(new MyComparableObject(4));
        myComparableObjectList.add(new MyComparableObject(5));
        myComparableObjectList.add(new MyComparableObject(1));
        Collections.sort(myComparableObjectList);
        System.out.println(myComparableObjectList);
    }

    @Override
    public int compareTo(@Nonnull MyComparableObject that) {
        // 想要 priority 值越大越优先，应该用写
        // return this.priority - that.priority
        // 还是写
        // return that.priority - this.priority
        // ?
        //
        // 应该写 return that.priority - this.priority;
        // 因为：
        // Comparable.compareTo(Object that) 的 javadoc 中写明
        // Compares this object with the specified object for order.
        // Returns a negative integer, zero, or a positive integer as this object
        // is less than, equal to, or greater than the specified object.
        //
        // 且
        // Collections.sort() 的 javadoc 中写明
        // Sorts the specified list into ascending order, according to the natural ordering of its elements.
        // 所以应该是 return that.priority - this.priority;
        return that.priority - this.priority;
    }

    @Override
    public String toString() {
        return "MyComparableObject{" +
                "priority=" + priority +
                '}' + Integer.toHexString(hashCode());
    }
}
