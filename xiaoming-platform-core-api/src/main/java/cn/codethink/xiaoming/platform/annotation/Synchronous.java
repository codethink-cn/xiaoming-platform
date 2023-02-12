package cn.codethink.xiaoming.platform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>同步</h1>
 *
 * <p>同步注解将告诉小明平台必须在主线程上执行某些方法以保证程序正常运行。</p>
 *
 * <ui>
 *     <li>当该注解作用在方法上时，表明该方法应该被同步回调。</li>
 *     <li>当该注解作用在类上时，表明该类及其所有内部类（一级、二级等）中的方法都应该被同步回调。</li>
 * </ui>
 *
 * @author Chuanwise
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Synchronous {
}
