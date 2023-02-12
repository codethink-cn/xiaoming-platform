package cn.codethink.xiaoming.platform.event;

import cn.codethink.xiaoming.platform.object.PlatformObject;
import cn.codethink.xiaoming.platform.registration.Registration;

/**
 * <h1>监听器注册项</h1>
 *
 * @author Chuanwise
 */
public interface ListenerRegistration
    extends Registration {
    
    /**
     * 监听器优先级
     *
     * @return 优先级
     */
    int getPriority();
    
    /**
     * 监听器
     *
     * @return 监听器
     */
    Listener<?> getListener();
}
