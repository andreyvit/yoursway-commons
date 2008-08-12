package com.yoursway.utils.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This can be used to modify other contracts. Cannot be used without specifying
 * a multithreading contact.
 */
@Retention(SOURCE)
@Target( { METHOD })
public @interface ReentrantFromTheSameThread {
    
}
