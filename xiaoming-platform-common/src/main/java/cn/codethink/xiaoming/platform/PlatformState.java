package cn.codethink.xiaoming.platform;

/**
 * 平台状态
 *
 * @author Chuanwise
 */
public enum PlatformState {
    
    /**
     * 初始、空闲状态
     */
    IDLE,
    
    /**
     * 正在启动状态
     */
    STARTING,
    
    /**
     * 已启动状态
     */
    STARTED,
    
    /**
     * 启动错误状态
     */
    START_ERROR,
    
    /**
     * 正在关闭状态
     */
    STOPPING,
    
    /**
     * 关闭错误状态
     */
    STOP_ERROR,
    
    /**
     * 严重错误状态
     */
    FATAL_ERROR
}
