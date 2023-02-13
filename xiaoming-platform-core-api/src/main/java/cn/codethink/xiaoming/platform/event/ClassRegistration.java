package cn.codethink.xiaoming.platform.event;

import cn.codethink.xiaoming.platform.registration.Registration;

/**
 * <h1>类型注册</h1>
 *
 * <p>基于类型继承关系的注册</p>
 *
 * @param <T> 类型
 */
public interface ClassRegistration<T>
    extends Registration {
    
    /**
     * 获取注册类型
     *
     * @return 注册类型
     */
    Class<T> getRegisteredClass();
}
