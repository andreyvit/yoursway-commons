package com.yoursway.utils.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This can be used to mark generally reentrant methods. When used without any
 * multithreading contract annotations, <code>@UseFromAnyThread</code> is
 * assumed.
 */
@Retention(SOURCE)
@Target( { METHOD })
public @interface Reentrant_CallFromAnyThread {
    
}
