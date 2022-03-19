package cn.codethink.xiaoming.platform.plugin;

import cn.codethink.xiaoming.logger.Logger;
import cn.codethink.xiaoming.platform.PlatformObject;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * 插件管理器
 *
 * @author Chuanwise
 */
public interface PluginManager
        extends PlatformObject {
    
    /**
     * 获取插件文件夹
     *
     * @return 插件文件夹
     */
    File getDirectory();
    
    /**
     * 刷新插件表
     */
    void flushPluginTables();
    
    /**
     * 获取插件控制器
     *
     * @return 插件控制器
     */
    List<PluginHandler> getPluginHandlers();
    
    /**
     * 获取插件控制器
     *
     * @param pluginName 插件名
     * @return 当找到该插件返回该插件，否则返回 null
     */
    PluginHandler getPluginHandler(String pluginName);
    
    /**
     * 获取插件控制器
     *
     * @param uuid 插件 UUID
     * @return 当找到该插件返回该插件，否则返回 null
     */
    PluginHandler getPluginHandler(UUID uuid);
    
    /**
     * 添加插件
     *
     * @param pluginHandler 对象插件
     */
    void addPluginHandler(PluginHandler pluginHandler);
    
    /**
     * 加载所有能加载的插件
     */
    void loadPlugins();
    
    /**
     * 启动所有能启动的插件
     */
    void enablePlugins();
    
    /**
     * 关闭所有能关闭的插件
     */
    void disablePlugins();
    
    /**
     * 卸载所有能卸载的插件
     */
    void unloadPlugins();
    
    /**
     * 获得插件管理器日志
     *
     * @return 插件管理器日志
     */
    Logger getLogger();
}
