package cn.codethink.xiaoming.platform.annotation;

import java.lang.annotation.*;

/**
 * 交互器所需权限注解
 *
 * @author Chuanwise
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(Requires.class)
public @interface Require {
    
    /**
     * 获取交互器所需权限
     *
     * @return 交互器所需权限
     */
    String value();
}
