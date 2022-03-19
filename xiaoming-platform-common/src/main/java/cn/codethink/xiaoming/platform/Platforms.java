package cn.codethink.xiaoming.platform;

import cn.codethink.common.util.Preconditions;
import cn.codethink.common.util.StaticUtilities;
import cn.codethink.xiaoming.InstantMessenger;
import cn.codethink.xiaoming.annotation.InternalAPI;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 平台驱动器
 *
 * @author Chuanwise
 */
@InternalAPI
public final class Platforms
    extends StaticUtilities {
    
    /**
     * 平台驱动
     */
    static final Map<InstantMessenger, PlatformDriver> DRIVERS = new ConcurrentHashMap<>();
    
    /**
     * 获取平台驱动实例
     *
     * @return 驱动实例
     */
    public static Map<InstantMessenger, PlatformDriver> getDrivers() {
        return Collections.unmodifiableMap(DRIVERS);
    }
    
    /**
     * 获取通讯软件平台驱动
     *
     * @param instantMessenger 通讯软件
     * @return 当找到驱动，返回驱动，否则返回 null
     */
    public static PlatformDriver getDriver(InstantMessenger instantMessenger) {
        Preconditions.namedArgumentNonNull(instantMessenger, "instant messenger");
        
        return DRIVERS.get(instantMessenger);
    }
    
    /**
     * 注册通讯软件平台驱动
     *
     * @param instantMessenger 通讯软件
     * @param platformDriver   平台驱动
     */
    public static void registerDriver(InstantMessenger instantMessenger, PlatformDriver platformDriver) {
        Preconditions.namedArgumentNonNull(instantMessenger, "instant messenger");
        Preconditions.namedArgumentNonNull(platformDriver, "platform driver");
        
        DRIVERS.put(instantMessenger, platformDriver);
    }
}
