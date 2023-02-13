package cn.codethink.xiaoming.platform.annotation;

import cn.codethink.xiaoming.platform.event.Priority;
import cn.codethink.xiaoming.platform.registration.Subject;

import java.lang.annotation.*;

/**
 * <h1>监听器</h1>
 *
 * <p>监听器是事件发生时会被调用的一段代码，由主体 {@link Subject} 注册。</p>
 *
 * @author Chuanwise
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {
    
    /**
     * 监听器优先级
     *
     * @return 监听器优先级
     */
    Priority priority() default Priority.NORMAL;
    
    /**
     * 监听的事件类型
     *
     * @return 监听的事件类型
     */
    Class<?>[] classes() default {};
}
