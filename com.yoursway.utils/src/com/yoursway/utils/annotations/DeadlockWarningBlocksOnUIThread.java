package com.yoursway.utils.annotations;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(SOURCE)
@Target( { METHOD, CONSTRUCTOR })
public @interface DeadlockWarningBlocksOnUIThread {
    
}
