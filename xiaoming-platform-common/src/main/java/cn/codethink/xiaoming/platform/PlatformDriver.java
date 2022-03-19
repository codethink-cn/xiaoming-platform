package cn.codethink.xiaoming.platform;

import cn.codethink.xiaoming.platform.Platform;

/**
 * 平台驱动
 *
 * @author Chuanwise
 */
@FunctionalInterface
public interface PlatformDriver {
    
    /**
     * 生成默认平台的方法
     *
     * @return
     */
    Platform generate();
}
