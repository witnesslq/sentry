package me.flyness.sentry.annotation;

import java.lang.annotation.*;

/**
 * Created by bjlizhitao on 2016/9/6.
 * 监控方法注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sentry {
    /**
     * 监控方法的key，该key全局唯一，用于统计监控的方法的相关指标
     *
     * @return
     */
    String key() default "";
}
