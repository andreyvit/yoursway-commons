package com.yoursway.utils.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This is the default contract (External Synchronization Required and
 * Non-Reentrant). This annotation might be used to stress it.
 */
@Retention(SOURCE)
@Target( { METHOD })
public @interface NonReentrant_SynchronizeExternallyOrUseFromSingleThread {
    
}
