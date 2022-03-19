package cn.codethink.xiaoming.platform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Require 的容器
 *
 * @author Chuanwise
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requires {
    
    /**
     * 容器值
     *
     * @return Require 注解
     */
    Require[] value();
}
