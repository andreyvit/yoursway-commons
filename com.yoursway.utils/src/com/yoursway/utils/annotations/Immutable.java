package com.yoursway.utils.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Note: for methods use either <code>CallFromAnyThread_NonReentrant</code> or
 * <code>Reentrant_CallFromAnyThread</code> instead.
 */
@Retention(SOURCE)
@Target( { TYPE })
public @interface Immutable {
    
}
