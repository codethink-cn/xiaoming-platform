package cn.codethink.xiaoming.platform.plugin;

import cn.codethink.xiaoming.platform.PlatformObject;

/**
 * 插件控制器
 *
 * @author Chuanwise
 */
public interface PluginHandler
    extends PlatformObject {
    
    /**
     * 获取插件实例
     *
     * @return 插件实例
     */
    Plugin getPlugin();
    
    /**
     * 获取插件状态
     *
     * @return 插件状态
     */
    PluginState getState();
    
    /**
     * 获取插件类加载器
     *
     * @return 插件类加载器
     */
    PluginClassLoader getClassLoader();
    
    /**
     * 插件元数据
     *
     * @return 元数据
     */
    PluginMetadata getMetadata();
    
    /**
     * 询问插件是否加载
     *
     * @return 插件是否加载
     */
    boolean isLoaded();
    
    /**
     * 询问插件是否启动
     *
     * @return 插件是否启动
     */
    boolean isEnabled();
    
    /**
     * 询问插件是否关闭
     *
     * @return 插件是否关闭
     */
    boolean isDisabled();
    
    /**
     * 载入插件
     *
     * @return 是否成功载入插件
     */
    boolean loadPlugin();
    
    /**
     * 启动插件
     *
     * @return 是否成功启动插件
     */
    boolean enablePlugin();
    
    /**
     * 关闭插件
     *
     * @return 是否成功关闭插件
     */
    boolean disablePlugin();
    
    /**
     * 卸载插件
     *
     * @return 是否成功卸载插件
     */
    boolean unloadPlugin();
}
