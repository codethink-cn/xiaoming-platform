package cn.codethink.xiaoming.platform.annotation;

import cn.codethink.xiaoming.platform.registration.rule.BeforeAndAfterRule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>此前</h1>
 *
 * <p>该注解用于规定当前注册项和其他注册项之间的前后关系，影响注册项响应的顺序</p>
 *
 * @author Chuanwise
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
public @interface Before {
    
    /**
     * 获取注册项名
     *
     * @return 当前注册项应该在哪项注册项之前
     */
    String[] value();
    
    /**
     * 获取规则类型
     *
     * @return 规则类型
     */
    BeforeAndAfterRule.Level type() default BeforeAndAfterRule.Level.NORMAL;
}
