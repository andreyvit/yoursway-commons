package com.yoursway.utils.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This is the default contract (External Synchronization Required). This
 * annotation might be used to stress it.
 */
@Retention(SOURCE)
@Target( { TYPE, FIELD })
public @interface SynchronizeExternallyOrUseFromSingleThread {
    
}
