package com.yoursway.utils.annotations;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Note: for methods use either <code>CallFromAnyThread_NonReentrant</code> or
 * <code>Reentrant_CallFromAnyThread</code> instead.
 */
@Retention(SOURCE)
@Target( { TYPE, FIELD, CONSTRUCTOR })
public @interface UseFromAnyThread {
    
}
