package cn.codethink.xiaoming.platform.annotation;

import cn.codethink.xiaoming.platform.subject.Subject;

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
}
