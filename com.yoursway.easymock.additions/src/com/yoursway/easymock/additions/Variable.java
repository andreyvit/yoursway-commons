package com.yoursway.easymock.additions;

import static org.easymock.EasyMock.reportMatcher;

import org.easymock.IArgumentMatcher;

public class Variable<T> {
    
    private final String name;
    private final Class<T> klass;

    public Variable(Class<T> klass) {
        this(klass, klass.getSimpleName());
    }
    
    public Variable(Class<T> klass, String name) {
        if (name == null)
            throw new NullPointerException("name is null");
        if (klass == null)
            throw new NullPointerException("klass is null");
        this.name = name;
        this.klass = klass;
    }
    
    private T value;
    
    public T getValue() {
        return value;
    }
    
    public T extractValue() {
        T result = value;
        value = null;
        return result;
    }
    
    public void setValue(T value) {
        this.value = value;
    }
    
    public boolean isAssignableFrom(Object argument) {
        if (argument == null)
            return true;
        return klass.isAssignableFrom(argument.getClass());
    }

    @Override
    public String toString() {
        return name;
    }
    
    public static <T> T anyObjectSavingInto(final Variable<T> variable) {
        reportMatcher(new IArgumentMatcher() {

            @SuppressWarnings("unchecked")
            public boolean matches(Object argument) {
                if (!variable.isAssignableFrom(argument))
                    throw new IllegalArgumentException("Wrong argument type: " + argument);
                variable.setValue((T) argument);
                return true;
            }
            
            public void appendTo(StringBuffer buffer) {
                buffer.append("anyObject");
            }
            
        });
        return null;
    }

    public static <T> T eq(final Variable<T> variable) {
        reportMatcher(new IArgumentMatcher() {
            
            public boolean matches(Object argument) {
                return argument == variable.getValue();
            }
            
            public void appendTo(StringBuffer buffer) {
                buffer.append("eq(" + variable + ")");
            }
            
        });
        return null;
    }
    
    public static <T> Variable<T> create(Class<T> klass) {
        return new Variable<T>(klass);
    }
    
}
