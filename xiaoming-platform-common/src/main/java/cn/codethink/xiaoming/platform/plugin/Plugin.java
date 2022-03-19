package cn.codethink.xiaoming.platform.plugin;

import cn.codethink.xiaoming.platform.PlatformObject;

/**
 * 插件核心 API
 *
 * @author Chuanwise
 */
public interface Plugin
    extends PlatformObject {
    
    /**
     * 获取插件控制器
     *
     * @return 插件控制器
     */
    PluginHandler getPluginHandler();
    
    /**
     * 插件载入时回调
     *
     * @throws Exception 载入异常
     */
    default void onLoad() throws Exception {
    }
    
    /**
     * 插件启动时回调
     *
     * @throws Exception 启动载入异常
     */
    default void onEnable() throws Exception {
    }
    
    /**
     * 插件关闭时回调
     *
     * @throws Exception 关闭异常
     */
    default void onDisable() throws Exception {
    }
}
