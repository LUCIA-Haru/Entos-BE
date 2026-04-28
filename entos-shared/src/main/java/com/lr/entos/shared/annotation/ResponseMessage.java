package com.lr.entos.shared.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE}) // Added TYPE to allow class-level defaults
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseMessage {
    String value() default "";

    /**
     * Whether to translate the message using the system's Locale.
     * Useful if Entos targets different regions later.
     */
    boolean localizable() default false;

}
