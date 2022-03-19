package cn.codethink.xiaoming.platform.concurrent;

import cn.codethink.common.api.ExceptionRunnable;
import cn.codethink.common.api.ExceptionSupplier;
import cn.codethink.xiaoming.concurrent.PeriodBotTask;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.plugin.Plugin;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 调度器
 *
 * @author Chuanwise
 */
public interface Scheduler {
    
    /**
     * 执行一个任务
     *
     * @param action 任务动作
     * @param plugin 提交插件
     * @return 任务
     */
    PlatformTask submit(ExceptionRunnable action, Plugin plugin);
    
    /**
     * 稍后执行一个任务
     *
     * @param action 任务动作
     * @param plugin 提交插件
     * @param delay 延迟
     * @return 任务
     */
    PlatformTask schedule(ExceptionRunnable action, Plugin plugin, long delay);
    
    /**
     * 稍后执行一个任务
     *
     * @param action 任务动作
     * @param plugin 提交插件
     * @param delay 延迟
     * @param timeUnit 时间单位
     * @return 任务
     */
    PlatformTask schedule(ExceptionRunnable action, Plugin plugin, long delay, TimeUnit timeUnit);
    
    /**
     * 执行一个任务
     *
     * @param action 任务动作
     * @param plugin 提交插件
     * @return 任务
     */
    <T> PlatformFuture<T> submit(Callable<T> action, Plugin plugin);
    
    /**
     * 稍后执行一个任务
     *
     * @param action 任务动作
     * @param plugin 提交插件
     * @param delay 延迟
     * @return 任务
     */
    <T> PlatformFuture<T> schedule(Callable<T> action, Plugin plugin, long delay);
    
    /**
     * 稍后执行一个任务
     *
     * @param action 任务动作
     * @param plugin 提交插件
     * @param delay 延迟
     * @param timeUnit 时间单位
     * @return 任务
     */
    <T> PlatformFuture<T> schedule(Callable<T> action, Plugin plugin, long delay, TimeUnit timeUnit);
    
    /**
     * 取消某个插件的任务
     *
     * @param plugin 插件
     * @return 被取消的任务
     */
    List<PlatformTask> cancelTasks(Plugin plugin, boolean interrupt);
    
    /**
     * 取消现在正在进行的所有任务
     *
     * @return 被取消的任务
     */
    List<PlatformTask> cancelAllTasks(boolean interrupt);
    
    
    /**
     * 执行一个定时任务，任务之间有相同的间隔
     *
     * @param action 动作
     * @param plugin 提交插件
     * @param period 周期
     * @return 定时任务
     */
    PeriodPlatformTask scheduleWithFixedDelay(ExceptionRunnable action, Plugin plugin, long period);
    
    /**
     * 执行一个定时任务，任务之间有相同的间隔
     *
     * @param action   动作
     * @param plugin 提交插件
     * @param period   周期
     * @param timeUnit 时间单位
     * @return 定时任务
     */
    PeriodPlatformTask scheduleWithFixedDelay(ExceptionRunnable action, Plugin plugin, long period, TimeUnit timeUnit);
    
    /**
     * 执行一个定时任务，任务之间严格按照周期调度
     *
     * @param action 动作
     * @param plugin 提交插件
     * @param period 周期
     * @return 定时任务
     */
    PeriodPlatformTask scheduleAtFixedRate(ExceptionRunnable action, Plugin plugin, long period);
    
    /**
     * 执行一个定时任务，任务之间严格按照周期调度
     *
     * @param action   动作
     * @param plugin 提交插件
     * @param period   周期
     * @param timeUnit 时间单位
     * @return 定时任务
     */
    PeriodPlatformTask scheduleAtFixedRate(ExceptionRunnable action, Plugin plugin, long period, TimeUnit timeUnit);
    
    /**
     * 询问调度器是否关闭
     *
     * @return 调度器是否关闭
     */
    boolean isShutdown();
    
    /**
     * 询问所有任务是否已执行完
     *
     * @return 所有任务是否已执行完
     */
    boolean isTerminated();
    
    /**
     * 优雅地关闭调度器
     */
    void shutdownGracefully();
    
    /**
     * 立刻关闭调度器
     */
    void shutdownImmediately();
    
    /**
     * 等待所有任务执行结束
     *
     * @param timeout 超时时长
     * @return 是否在时间范围内所有任务结束了
     * @throws InterruptedException 中断异常
     */
    boolean awaitTermination(long timeout) throws InterruptedException;
    
    /**
     * 不可打断地等待所有任务执行结束
     *
     * @param timeout 超时时长
     * @return 是否在时间范围内所有任务结束了
     */
    boolean awaitTerminationUninterruptibly(long timeout);
    
    /**
     * 等待所有任务执行结束
     *
     * @param timeout  超时时长
     * @param timeUnit 事件单位
     * @return 是否在时间范围内所有任务结束了
     * @throws InterruptedException 中断异常
     */
    boolean awaitTermination(long timeout, TimeUnit timeUnit) throws InterruptedException;
    
    /**
     * 不可打断地等待所有任务执行结束
     *
     * @param timeout  超时时长
     * @param timeUnit 事件单位
     * @return 是否在时间范围内所有任务结束了
     */
    boolean awaitTerminationUninterruptibly(long timeout, TimeUnit timeUnit);
    
    /**
     * 等待所有任务执行结束
     */
    void awaitTermination() throws InterruptedException;
    
    /**
     * 不可中断地等待所有任务执行结束
     */
    void awaitTerminationUninterruptibly();
    
    /**
     * 同步关闭调度器
     *
     * @throws InterruptedException 中断异常
     */
    void shutdownSync() throws InterruptedException;
    
    /**
     * 同步关闭调度器
     *
     * @param timeout 超时时长
     * @return 是否在时间范围内所有任务结束了
     * @throws InterruptedException 中断异常
     */
    boolean shutdownSync(long timeout) throws InterruptedException;
    
    /**
     * 同步关闭调度器
     *
     * @param timeout  超时时长
     * @param timeUnit 时间单位
     * @return 是否在时间范围内所有任务结束了
     * @throws InterruptedException 中断异常
     */
    boolean shutdownSync(long timeout, TimeUnit timeUnit) throws InterruptedException;
    
    /**
     * 不可打断地同步关闭调度器
     */
    void shutdownUninterruptibly();
    
    /**
     * 不可打断地同步关闭调度器
     *
     * @param timeout 超时时长
     * @return 是否在时间范围内所有任务结束了
     */
    boolean shutdownUninterruptibly(long timeout);
    
    /**
     * 不可打断地同步关闭调度器
     *
     * @param timeout  超时时长
     * @param timeUnit 时间单位
     * @return 是否在时间范围内所有任务结束了
     */
    boolean shutdownUninterruptibly(long timeout, TimeUnit timeUnit);
}
