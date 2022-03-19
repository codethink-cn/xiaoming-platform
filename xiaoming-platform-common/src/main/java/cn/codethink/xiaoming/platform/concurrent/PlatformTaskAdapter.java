package cn.codethink.xiaoming.platform.concurrent;

import cn.codethink.xiaoming.platform.plugin.Plugin;
import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class PlatformTaskAdapter
        implements PlatformTask {
    
    protected final PlatformTask task;
    
    @Override
    public boolean cancel(boolean interrupt) {
        return task.cancel(interrupt);
    }
    
    @Override
    public boolean isSucceed() {
        return task.isSucceed();
    }
    
    @Override
    public boolean isFailed() {
        return task.isFailed();
    }
    
    @Override
    public boolean isDone() {
        return task.isDone();
    }
    
    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }
    
    @Override
    public Throwable getCause() {
        return task.getCause();
    }
    
    @Override
    public PlatformTask addListener(PlatformTaskListener listener) {
        return task.addListener(listener);
    }
    
    @Override
    public PlatformTask addListeners(PlatformTaskListener... listeners) {
        return task.addListeners(listeners);
    }
    
    @Override
    public PlatformTask addListeners(Iterable<? extends PlatformTaskListener> listeners) {
        return task.addListeners(listeners);
    }
    
    @Override
    public PlatformTask await() throws InterruptedException {
        return task.await();
    }
    
    @Override
    public boolean await(long timeout) throws InterruptedException {
        return task.await(timeout);
    }
    
    @Override
    public boolean await(long timeout, TimeUnit timeUnit) throws InterruptedException {
        return task.await(timeout, timeUnit);
    }
    
    @Override
    public PlatformTask awaitUninterruptibly() {
        return task.awaitUninterruptibly();
    }
    
    @Override
    public boolean awaitUninterruptibly(long timeout) {
        return task.awaitUninterruptibly(timeout);
    }
    
    @Override
    public boolean awaitUninterruptibly(long timeout, TimeUnit timeUnit) {
        return task.awaitUninterruptibly(timeout, timeUnit);
    }
    
    @Override
    public PlatformTask sync() throws InterruptedException {
        return task.sync();
    }
    
    @Override
    public PlatformTask syncUninterruptibly() {
        return task.syncUninterruptibly();
    }
    
    @Override
    public Plugin getPlugin() {
        return task.getPlugin();
    }
}
