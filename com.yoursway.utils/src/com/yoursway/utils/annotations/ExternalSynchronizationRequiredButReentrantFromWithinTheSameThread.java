package com.yoursway.utils.annotations;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(SOURCE)
@Target( { TYPE, METHOD, FIELD, CONSTRUCTOR })
public @interface ExternalSynchronizationRequiredButReentrantFromWithinTheSameThread {
    
}
