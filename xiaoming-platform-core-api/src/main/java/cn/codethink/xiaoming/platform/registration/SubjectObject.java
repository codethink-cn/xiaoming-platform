package cn.codethink.xiaoming.platform.registration;

import cn.codethink.xiaoming.platform.object.PlatformObject;

/**
 * <h1>主体对象</h1>
 *
 * <p>主体对象是对应主体的对象，如事件监听器。这类对象通常用 {@link Registration} 包装。</p>
 *
 * @author Chuanwise
 */
public interface SubjectObject
    extends PlatformObject {
    
    /**
     * 获取对应主体
     *
     * @return 对应主体
     */
    Subject getSubject();
}
