package cn.codethink.xiaoming.platform.annotation;

import cn.codethink.xiaoming.platform.permission.PermissionService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>服务</h1>
 *
 * <p>服务是一种接口，运行时由插件注册实现。例如权限服务 {@link PermissionService}。</p>
 *
 * @author Chuanwise
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
}
