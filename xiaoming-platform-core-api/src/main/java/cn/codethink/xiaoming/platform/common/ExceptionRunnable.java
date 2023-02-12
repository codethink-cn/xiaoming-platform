package cn.codethink.xiaoming.platform.common;

/**
 * <h1>可抛出异常的 Runnable</h1>
 *
 * @author Chuanwise
 */
@FunctionalInterface
public interface ExceptionRunnable {
    
    /**
     * 执行动作
     *
     * @throws Exception 处理过程中的异常
     */
    void run() throws Exception;
}