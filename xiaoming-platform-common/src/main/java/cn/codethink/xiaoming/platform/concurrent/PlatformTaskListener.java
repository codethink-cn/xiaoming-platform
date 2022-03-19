package cn.codethink.xiaoming.platform.concurrent;

/**
 * 平台任务监听器
 *
 * @author Chuanwise
 */
@FunctionalInterface
public interface PlatformTaskListener {
    
    /**
     * 任务结束后的回调方法
     *
     * @param platformTask 平台任务
     */
    void listen(PlatformTask platformTask);
}
