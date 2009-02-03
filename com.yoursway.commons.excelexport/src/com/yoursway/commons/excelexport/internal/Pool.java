package com.yoursway.commons.excelexport.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pool<T> {
    
    private final List<T> strings = new ArrayList<T>();
    
    private final List<T> unmodStrings = Collections.unmodifiableList(strings);
    
    private final Map<T, Integer> stringsToOrdinals = new HashMap<T, Integer>();
    
    public int add(T string) {
        Integer result = stringsToOrdinals.get(string);
        if (result != null)
            return result;
        else {
            int id = strings.size();
            strings.add(string);
            stringsToOrdinals.put(string, id);
            return id;
        }
    }
    
    public int retrieve(T string) {
        Integer result = stringsToOrdinals.get(string);
        if (result != null)
            return result;
        throw new IllegalArgumentException("Attemping to retrieve ID of unregistered pool item.");
    }
    
    public List<T> entireSequence() {
        return unmodStrings;
    }
    
    public static <T> Pool<T> create() {
        return new Pool<T>();
    }
    
}
