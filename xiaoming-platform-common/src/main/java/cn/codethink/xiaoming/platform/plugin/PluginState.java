package cn.codethink.xiaoming.platform.plugin;

/**
 * 插件状态
 *
 * @author Chuanwise
 */
public enum PluginState {
    
    /**
     * 空闲状态
     */
    IDLE,
    
    /**
     * 正在加载状态
     */
    LOADING,
    
    /**
     * 加载完成状态
     */
    LOADED,
    
    /**
     * 加载时出现异常
     */
    LOAD_ERROR,
    
    /**
     * 正在启动状态
     */
    ENABLING,
    
    /**
     * 启动完成状态
     */
    ENABLED,
    
    /**
     * 启动时出现异常
     */
    ENABLE_ERROR,
    
    /**
     * 正在关闭的状态
     */
    DISABLING,
    
    /**
     * 关闭完成的状态
     */
    DISABLED,
    
    /**
     * 关闭时出现错误
     */
    DISABLE_ERROR,
    
    /**
     * 正在卸载
     */
    UNLOADING,
    
    /**
     * 严重错误
     */
    FATAL_ERROR,
}
