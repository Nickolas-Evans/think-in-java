package com.tmp;

import java.util.ArrayList;
import java.util.Collection;

public class Temp {

    public static void main(String[] args) {
        Collection<Collection<?>> collections = new ArrayList<>();
        collections.add(collections);

        Object obj  = ((ArrayList<Collection<?>>) collections).clone();
    }
}
