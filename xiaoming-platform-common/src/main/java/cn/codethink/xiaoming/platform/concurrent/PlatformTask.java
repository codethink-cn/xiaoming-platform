package cn.codethink.xiaoming.platform.concurrent;

import cn.codethink.xiaoming.platform.plugin.PluginObject;

import java.util.concurrent.TimeUnit;

/**
 * @author Chuanwise
 */
public interface PlatformTask
        extends PluginObject {
    
    /**
     * 取消某个任务
     *
     * @param interrupt 当任务正在进行时，是否打断
     * @return 当任务已经结束、已经取消或因为其他原因无法取消时返回 false
     * 否则返回 true
     */
    boolean cancel(boolean interrupt);
    
    /**
     * 询问任务是否成功
     *
     * @return 任务是否成功
     */
    boolean isSucceed();
    
    /**
     * 询问任务是否失败
     *
     * @return 任务是否失败
     */
    boolean isFailed();
    
    /**
     * 询问任务是否结束
     *
     * @return 任务是否结束
     */
    boolean isDone();
    
    /**
     * 询问任务是否取消
     *
     * @return 任务是否取消
     */
    boolean isCancelled();
    
    /**
     * 获取失败异常
     *
     * @return 当任务执行失败，返回其异常，否则返回 null
     */
    Throwable getCause();
    
    /**
     * 添加任务监听器
     *
     * @param listener 任务监听器
     * @return 平台任务
     */
    PlatformTask addListener(PlatformTaskListener listener);
    
    /**
     * 添加若干任务监听器
     *
     * @param listeners 若干任务监听器
     * @return 平台任务
     */
    PlatformTask addListeners(PlatformTaskListener... listeners);
    
    /**
     * 添加若干任务监听器
     *
     * @param listeners 若干任务监听器
     * @return 平台任务
     */
    PlatformTask addListeners(Iterable<? extends PlatformTaskListener> listeners);
    
    /**
     * 阻塞当前线程，直到任务结束
     *
     * @return 平台任务
     * @throws InterruptedException 中断异常
     */
    PlatformTask await() throws InterruptedException;
    
    /**
     * 阻塞当前线程，直到任务结束
     *
     * @param timeout 超时时长
     * @return 当等到任务结束时返回 true，否则返回 false
     * @throws InterruptedException 中断异常
     */
    boolean await(long timeout) throws InterruptedException;
    
    /**
     * 阻塞当前线程，直到任务结束
     *
     * @param timeout 超时时长
     * @param timeUnit 时间单位
     * @return 当等到任务结束时返回 true，否则返回 false
     * @throws InterruptedException 中断异常
     */
    boolean await(long timeout, TimeUnit timeUnit) throws InterruptedException;
    
    /**
     * 不可打断地阻塞当前线程，直到任务结束
     *
     * @return 平台任务
     */
    PlatformTask awaitUninterruptibly();
    
    /**
     * 不可打断地阻塞当前线程，直到任务结束
     *
     * @param timeout 超时时长
     * @return 当等到任务结束时返回 true，否则返回 false
     */
    boolean awaitUninterruptibly(long timeout);
    
    /**
     * 不可打断地阻塞当前线程，直到任务结束
     *
     * @param timeout 超时时长
     * @param timeUnit 时间单位
     * @return 平台任务
     */
    boolean awaitUninterruptibly(long timeout, TimeUnit timeUnit);
    
    /**
     * 阻塞当前线程，直到任务结束
     *
     * @return 平台任务
     * @throws InterruptedException 中断异常
     */
    PlatformTask sync() throws InterruptedException;
    
    /**
     * 不可打断地阻塞当前线程，直到任务结束
     *
     * @return 平台任务
     */
    PlatformTask syncUninterruptibly();
}