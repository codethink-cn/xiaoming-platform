package cn.codethink.xiaoming.platform.event;

import cn.codethink.xiaoming.platform.registration.Subject;

/**
 * <h1>事件环境</h1>
 *
 * <p>事件环境是事件发布时的环境</p>
 *
 * @author Chuanwise
 */
public interface EventContext {
    
    /**
     * 获取事件
     *
     * @param <T> 事件类型
     * @return 事件对象
     */
    <T> T getEvent();
    
    /**
     * 获取事件发布者
     *
     * @return 事件发布者
     */
    Subject getSubject();
}
