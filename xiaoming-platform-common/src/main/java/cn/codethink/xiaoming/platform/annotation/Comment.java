package cn.codethink.xiaoming.platform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Yaml 注解，用于注解类或属性
 *
 * @author Chuanwise
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment {
    
    /**
     * 获取注解内容
     *
     * @return 注解内容
     */
    String value();
}
