package com.xxl.job.core.handler.annotation;

import java.lang.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * annotation for job handler
 * @author 2016-5-17 21:06:49
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface JobHandler {

    String value() default "";
}
