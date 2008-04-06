package com.yoursway.progress.core.tests.internal;

import java.io.IOException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Join is the only utility for joining pieces of text separated by a delimiter
 * that you will ever need. It can handle Iterators, Collections, arrays, and
 * varargs, and can append to any Appendable or just return a String.
 * <p>
 * A trivial example: {@code join(":", "a", "b", "c")} gives {@code "a:b:c"}.
 * See JoinTest for more examples.
 * <p>
 * All the methods of this class throw {@link NullPointerException} when a value
 * of {@code null} is supplied for any parameter. The elements within the
 * collection, iterator, array, or varargs parameter list <i>may</i> be null --
 * these will be represented in the output with the string {@code "null"}.
 * 
 * @author Kevin Bourrillion
 */
public final class Join {
    private Join() {
    }
    
    /**
     * Appends each of the {@code tokens} to {@code appendable}, separated by
     * {@code delimiter}.
     * 
     * @param appendable
     *            the non-null object to append the results to
     * @param delimiter
     *            a non-null String to append between every element, but not at
     *            the beginning or end
     * @param tokens
     *            Objects of any type. For each element, if it is an instance of
     *            {@link CharSequence} it will be appended as-is, otherwise it
     *            will be converted to a {@code CharSequence} using
     *            {@link String#valueOf(Object)}. Note that this implies that
     *            null tokens will be appended as the four-character string
     *            {@code "null"}.
     * @return The same appendable instance that was passed in
     * @throws JoinException
     *             if an IOException occurs
     */
    public static Appendable join(Appendable appendable, String delimiter, Iterator<?> tokens) {
        
        /* This method is the workhorse of the class */

        checkNotNull(appendable);
        checkNotNull(delimiter);
        checkNotNull(tokens);
        if (tokens.hasNext()) {
            try {
                appendOneToken(appendable, tokens.next());
                while (tokens.hasNext()) {
                    appendable.append(delimiter);
                    appendOneToken(appendable, tokens.next());
                }
            } catch (IOException e) {
                throw new JoinException(e);
            }
        }
        return appendable;
    }
    
    /**
     * Returns a String containing the {@code tokens}, converted to Strings if
     * necessary, separated by {@code delimiter}. If {@code tokens} is empty,
     * returns the empty string.
     */
    public static String join(String delimiter, Iterable<?> tokens) {
        checkNotNull(tokens);
        return join(delimiter, tokens.iterator());
    }
    
    /**
     * Variant of {@link #join(Appendable,String,Iterator)} where {@code tokens}
     * is an {@code Iterable}.
     */
    public static Appendable join(Appendable appendable, String delimiter, Iterable<?> tokens) {
        checkNotNull(tokens);
        return join(appendable, delimiter, tokens.iterator());
    }
    
    /**
     * Variant of {@link #join(Appendable,String,Iterator)} where {@code tokens}
     * is an array.
     */
    public static Appendable join(Appendable appendable, String delimiter, Object[] tokens) {
        checkNotNull(tokens);
        return join(appendable, delimiter, Arrays.asList(tokens));
    }
    
    /**
     * Variant of {@link #join(Appendable,String,Iterator)} for tokens given
     * using varargs.
     */
    public static Appendable join(Appendable appendable, String delimiter, Object firstToken,
            Object... otherTokens) {
        checkNotNull(otherTokens);
        return join(appendable, delimiter, asList(firstToken, otherTokens));
    }
    
    /**
     * Variant of {@link #join(String,Iterable)} where {@code tokens} is an
     * {@code Iterator}.
     */
    public static String join(String delimiter, Iterator<?> tokens) {
        StringBuilder sb = new StringBuilder();
        join(sb, delimiter, tokens);
        return sb.toString();
    }
    
    /**
     * Variant of {@link #join(String,Iterable)} where {@code tokens} is an
     * array.
     */
    public static String join(String delimiter, Object[] tokens) {
        checkNotNull(tokens);
        return join(delimiter, Arrays.asList(tokens));
    }
    
    /**
     * Variant of {@link #join(String,Iterable)} for tokens given using varargs.
     */
    public static String join(String delimiter, Object firstToken, Object... otherTokens) {
        checkNotNull(otherTokens);
        return join(delimiter, asList(firstToken, otherTokens));
    }
    
    private static void checkNotNull(Object x) {
        if (x == null)
            throw new NullPointerException();
    }
    
    private static void appendOneToken(Appendable appendable, Object token) throws IOException {
        appendable.append(toCharSequence(token));
    }
    
    private static CharSequence toCharSequence(Object token) {
        return (token instanceof CharSequence) ? (CharSequence) token : String.valueOf(token);
    }
    
    /**
     * Thrown in response to an {@link IOException} from the supplied
     * {@link Appendable}. This is used because most callers won't want to
     * worry about catching an IOException.
     */
    public static class JoinException extends RuntimeException {
        private JoinException(IOException cause) {
            super(cause);
        }
        
        private static final long serialVersionUID = 1L;
    }
    
    /**
     * Duplicate of
     * {@link com.google.common.collect.Lists#asList(Object, Object[])}. copied
     * here to remove dependencies.
     */
    private static List<Object> asList(final Object first, final Object[] rest) {
        return new AbstractList<Object>() {
            @Override
            public int size() {
                return rest.length + 1;
            }
            
            @Override
            public Object get(int index) {
                return (index == 0) ? first : rest[index - 1];
            }
        };
    }
}
