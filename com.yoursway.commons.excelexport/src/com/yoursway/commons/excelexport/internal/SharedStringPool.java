package com.yoursway.commons.excelexport.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedStringPool {
    
    private final List<String> strings = new ArrayList<String>();
    
    private final List<String> unmodStrings = Collections.unmodifiableList(strings);
    
    private final Map<String, Integer> stringsToOrdinals = new HashMap<String, Integer>();
    
    public int add(String string) {
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
    
    public List<String> entireSequence() {
        return unmodStrings;
    }
    
}
