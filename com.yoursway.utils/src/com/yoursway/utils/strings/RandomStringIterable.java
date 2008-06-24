package com.yoursway.utils.strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class RandomStringIterable implements Iterable<String> {
    
    private final String[] choices;
    
    public RandomStringIterable(String[] choices) {
        this.choices = choices;
    }
    
    public Iterator<String> iterator() {
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(choices));
        Collections.shuffle(list);
        return list.iterator();
    }
    
}
