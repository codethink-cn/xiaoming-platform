package cn.codethink.xiaoming.platform.concurrent;

import cn.codethink.common.util.Executors;
import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.plugin.Plugin;
import lombok.Data;

/**
 * 简单平台任务
 *
 * @author Chuanwise
 */
@Data
@SuppressWarnings("all")
public abstract class AbstractThreadPlatformTask
        extends AbstractPlatformTask
        implements Runnable {
    
    /**
     * 任务状态
     */
    protected enum State {
    
        /**
         * 初始化状态
         */
        INITIALIZED,
    
        /**
         * 正在执行状态
         */
        EXECUTING,
    
        /**
         * 执行成功状态
         */
        SUCCEED,
    
        /**
         * 执行失败状态
         */
        FAILED,
    
        /**
         * 取消状态
         */
        CANCELLED,
    }
    
    /**
     * 任务状态
     */
    protected volatile State state = State.INITIALIZED;
    
    /**
     * 执行任务的线程，只有在 {@link #state} 为 {@link State#EXECUTING} 时非空
     */
    private volatile Thread thread;
    
    /**
     * 该中断异常是否是因为取消出现的
     */
    private volatile boolean cancellingInInterrupt;
    
    /**
     * 任务结束信号量
     */
    private final Object mutex = new Object();
    
    public AbstractThreadPlatformTask(Platform platform, Plugin plugin) {
        super(platform, plugin);
    }
    
    private void setFinalState(State state) {
        // set state
        this.state = state;
    
        // notify all
        synchronized (mutex) {
            mutex.notifyAll();
        }
        
        // call listeners
        for (PlatformTaskListener listener : listeners) {
            listener.listen(this);
        }
    }
    
    @Override
    public boolean cancel(boolean interrupt) {
        switch (state) {
            case INITIALIZED:
                setFinalState(State.CANCELLED);
                return true;
            case EXECUTING:
                setFinalState(State.CANCELLED);
                
                // 如果需要中断，则中断线程
                if (interrupt) {
                    try {
                        cancellingInInterrupt = true;
                        thread.interrupt();
                    } finally {
                        cancellingInInterrupt = false;
                    }
                }
                return true;
            case SUCCEED:
            case FAILED:
            case CANCELLED:
                return false;
            default:
                throw new IllegalStateException();
        }
    }
    
    @Override
    public boolean isSucceed() {
        return state == State.SUCCEED;
    }
    
    @Override
    public boolean isFailed() {
        return state == State.FAILED;
    }
    
    @Override
    public boolean isDone() {
        return state == State.SUCCEED
            || state == State.FAILED
            || state == State.CANCELLED;
    }
    
    @Override
    public boolean isCancelled() {
        return state == State.CANCELLED;
    }
    
    @Override
    public PlatformTask await() throws InterruptedException {
        switch (state) {
            case INITIALIZED:
            case EXECUTING:
                Executors.await(mutex);
                break;
            case SUCCEED:
            case FAILED:
            case CANCELLED:
                break;
            default:
                throw new IllegalStateException();
        }
        return this;
    }
    
    @Override
    public boolean await(long timeout) throws InterruptedException {
        Preconditions.argument(timeout >= 0, "time out must be bigger than or equals to 0!");
    
        switch (state) {
            case INITIALIZED:
            case EXECUTING:
                return Executors.await(mutex, timeout);
                
            case SUCCEED:
            case FAILED:
            case CANCELLED:
                return false;
            default:
                throw new IllegalStateException();
        }
    }
    
    @Override
    public PlatformTask awaitUninterruptibly() {
        switch (state) {
            case INITIALIZED:
            case EXECUTING:
                Executors.awaitUninterruptibly(mutex);
                break;
            case SUCCEED:
            case FAILED:
            case CANCELLED:
                break;
            default:
                throw new IllegalStateException();
        }
        
        return this;
    }
    
    @Override
    public boolean awaitUninterruptibly(long timeout) {
        Preconditions.argument(timeout >= 0, "time out must be bigger than or equals to 0!");
    
        switch (state) {
            case INITIALIZED:
            case EXECUTING:
                return Executors.awaitUninterruptibly(mutex, timeout);
            case SUCCEED:
            case FAILED:
            case CANCELLED:
                return false;
            default:
                throw new IllegalStateException();
        }
    }
    
    @Override
    public final void run() {
        switch (state) {
            case INITIALIZED:
                break;
            case EXECUTING:
                throw new IllegalStateException("can not run task in parallel");
            case SUCCEED:
            case FAILED:
                throw new IllegalStateException("task already executed");
            case CANCELLED:
                return;
            default:
                throw new IllegalStateException();
        }
        
        try {
            state = State.EXECUTING;
            thread = Thread.currentThread();
            
            run0();
    
            // state may be set to CANCELLED dute to uninterruptible cancelling
            if (state == State.EXECUTING) {
                setFinalState(State.SUCCEED);
            }
        } catch (Throwable throwable) {
            
            // if it's interruptible cancelling,
            // and the exception is an instanceo of InterruptedException,
            // obviously the exception was caused by cancelling
            if (cancellingInInterrupt
                && throwable instanceof InterruptedException) {
                return;
            }
            
            // record exception
            cause = throwable;
            setFinalState(State.FAILED);
        } finally {
            thread = null;
        }
    }
    
    protected abstract void run0() throws Exception;
}