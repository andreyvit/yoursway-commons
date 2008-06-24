package com.yoursway.utils.strings;

public class StringTupleIterable extends TupleIterable<String> {

    public StringTupleIterable(Iterable<String>[] elements) {
        super(String.class, elements);
    }
    
}
