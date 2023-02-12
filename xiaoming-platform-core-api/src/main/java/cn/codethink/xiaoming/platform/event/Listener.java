package cn.codethink.xiaoming.platform.event;

/**
 * <h1>监听器</h1>
 *
 * <p>在事件发生时执行的一段代码</p>
 *
 * @author Chuanwise
 */
@FunctionalInterface
public interface Listener {
    
    /**
     * 监听事件
     *
     * @param object 事件
     * @throws Exception 监听时抛出的异常
     */
    void listen(Object object) throws Exception;
}