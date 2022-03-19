package cn.codethink.xiaoming.platform.concurrent;

import cn.codethink.common.api.ExceptionRunnable;
import cn.codethink.common.util.Executors;
import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.Bot;
import cn.codethink.xiaoming.concurrent.AbstractBotTask;
import cn.codethink.xiaoming.concurrent.PeriodBotTask;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.plugin.Plugin;

/**
 * 周期性线程 BotTask
 *
 * @author Chuanwise
 */
public class PeriodThreadPlatformTask
        extends AbstractPlatformTask
        implements Runnable, PeriodPlatformTask {
    
    /**
     * 任务状态
     */
    private enum State {
        /**
         * 正在执行
         */
        EXECUTING,
    
        /**
         * 正在等待被执行
         */
        WAITING,
    
        /**
         * 已被取消
         */
        CANCELLED,
    
        /**
         * 只取消下一次
         */
        SKIPPING,
    
        /**
         * 出现异常
         */
        FAILURE,
    }
    
    /**
     * 任务状态
     */
    private volatile State state = State.WAITING;
    
    /**
     * 任务线程
     */
    private volatile Thread thread;
    
    /**
     * 任务行为
     */
    private final ExceptionRunnable action;
    
    /**
     * 信号量
     */
    private final Object mutex = new Object();
    
    private volatile boolean cancellingInInterrupt;
    
    public PeriodThreadPlatformTask(Platform platform, Plugin plugin, ExceptionRunnable action) {
        super(platform, plugin);
    
        Preconditions.namedArgumentNonNull(action, "action");
        
        this.action = action;
    }
    
    private void setFinalState(State state) {
        this.state = state;
    
        synchronized (mutex) {
            mutex.notifyAll();
        }
    }
    
    @Override
    @SuppressWarnings("all")
    public void run() {
        // discuss state
        switch (state) {
            case WAITING:
                break;
            case SKIPPING:
                state = State.WAITING;
                return;
            case CANCELLED:
            case FAILURE:
                return;
            case EXECUTING:
                throw new IllegalStateException("can not execute a period task in parallel");
            default:
                throw new IllegalStateException();
        }
        
        try {
            // 改变状态量
            state = State.EXECUTING;
            thread = Thread.currentThread();
            
            // 执行
            action.exceptRun();
    
            // 判断是否其他地方取消了，并且没有打断
            if (state == State.EXECUTING) {
                setFinalState(State.WAITING);
            }
        } catch (Throwable throwable) {
            
            if (cancellingInInterrupt
                && throwable instanceof InterruptedException) {
                return;
            }
    
            cause = throwable;
            setFinalState(State.FAILURE);
        } finally {
            thread = null;
        }
    }
    
    @Override
    public boolean cancel(boolean interrupt) {
        switch (state) {
            case WAITING:
            case SKIPPING:
                setFinalState(State.CANCELLED);
                return true;
            case EXECUTING:
                setFinalState(State.CANCELLED);
                if (interrupt) {
                    try {
                        cancellingInInterrupt = true;
                        
                        thread.interrupt();
                    } finally {
                        cancellingInInterrupt = false;
                    }
                }
                return true;
            case FAILURE:
            case CANCELLED:
                return false;
            default:
                throw new IllegalStateException();
        }
    }
    
    @Override
    public boolean isSucceed() {
        return state != State.FAILURE;
    }
    
    @Override
    public boolean isFailed() {
        return state == State.FAILURE;
    }
    
    @Override
    public boolean skip() {
        switch (state) {
            case WAITING:
            case SKIPPING:
            case EXECUTING:
                state = State.SKIPPING;
                return true;
            case FAILURE:
            case CANCELLED:
                return false;
            default:
                throw new IllegalStateException();
        }
    }
    
    @Override
    public boolean isCancelled() {
        return state == State.CANCELLED;
    }
    
    @Override
    public PlatformTask await() throws InterruptedException {
        return sync();
    }
    
    @Override
    public boolean isSkipping() {
        return state == State.SKIPPING;
    }
    
    @Override
    public boolean isDone() {
        return false;
    }
    
    @Override
    public PeriodPlatformTask sync() throws InterruptedException {
        switch (state) {
            case WAITING:
            case SKIPPING:
            case EXECUTING:
                
                Executors.await(mutex);
                
                break;
            case FAILURE:
            case CANCELLED:
                break;
            default:
                throw new IllegalStateException();
        }
        
        return this;
    }
    
    @Override
    public PeriodPlatformTask syncUninterruptibly() {
        switch (state) {
            case WAITING:
            case SKIPPING:
            case EXECUTING:
            
                Executors.awaitUninterruptibly(mutex);
            
                break;
            case FAILURE:
            case CANCELLED:
                break;
            default:
                throw new IllegalStateException();
        }
        
        return this;
    }
    
    @Override
    public boolean await(long timeout) throws InterruptedException {
        Preconditions.argument(timeout >= 0, "timeout must be bigger than or equals to 0!");
    
        switch (state) {
            case WAITING:
            case SKIPPING:
            case EXECUTING:
    
                return Executors.await(mutex, timeout);
                
            case FAILURE:
            case CANCELLED:
                return false;
            default:
                throw new IllegalStateException();
        }
    }
    
    @Override
    public PlatformTask awaitUninterruptibly() {
        return syncUninterruptibly();
    }
    
    @Override
    public boolean awaitUninterruptibly(long timeout) {
        Preconditions.argument(timeout >= 0, "timeout must be bigger than or equals to 0!");
    
        switch (state) {
            case WAITING:
            case SKIPPING:
            case EXECUTING:
            
                return Executors.awaitUninterruptibly(mutex, timeout);
        
            case FAILURE:
            case CANCELLED:
                return false;
            default:
                throw new IllegalStateException();
        }
    }
}
