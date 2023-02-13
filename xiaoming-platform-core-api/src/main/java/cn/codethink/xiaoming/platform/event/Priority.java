package cn.codethink.xiaoming.platform.event;

/**
 * <h1>优先级</h1>
 *
 * <p>表示动作优先次序</p>
 *
 * @author Chuanwise
 */
public enum Priority {
    
    /**
     * 初始化和注册操作的优先级
     */
    PRE,
    
    /**
     * 对初始化和注册操作的响应的优先级
     */
    AFTER_PRE,
    
    /**
     * 保护插件进行取消等操作的优先级
     */
    FIRST,
    
    /**
     * 在其他插件响应该事件之前应该进行的动作的优先级
     */
    EARLY,
    
    /**
     * 默认优先级
     */
    NORMAL,
    
    /**
     * 在其他插件响应该事件之后应该进行的动作的优先级
     */
    LATE,
    
    /**
     * 保护插件最终可以取消的优先级
     */
    LAST,
    
    /**
     * 需要对被取消等事件操作的优先级
     */
    BEFORE_POST,
    
    /**
     * 决定事件最终状态的优先级
     */
    POST,
}
