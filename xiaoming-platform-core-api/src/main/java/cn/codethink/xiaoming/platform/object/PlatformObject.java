package cn.codethink.xiaoming.platform.object;

import cn.codethink.xiaoming.platform.Platform;

/**
 * <h1>平台对象</h1>
 *
 * <p>平台对象是属于某一平台实例的对象，如插件管理器。</p>
 *
 * @author Chuanwise
 */
public interface PlatformObject {
    
    /**
     * 获取对应平台
     *
     * @return 对应平台
     */
    Platform getPlatform();
}
