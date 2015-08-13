package org.jeecg.learning.chapter3.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Result {

    String name() default "success";

    String location() default "";

    String type() default "";

    String[] params() default {};
}
