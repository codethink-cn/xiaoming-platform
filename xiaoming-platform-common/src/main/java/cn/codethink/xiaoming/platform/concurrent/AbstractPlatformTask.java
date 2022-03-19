package cn.codethink.xiaoming.platform.concurrent;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.concurrent.PlatformTask;
import cn.codethink.xiaoming.platform.concurrent.PlatformTaskListener;
import cn.codethink.xiaoming.platform.plugin.AbstractPluginObject;
import cn.codethink.xiaoming.platform.plugin.Plugin;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @see cn.codethink.xiaoming.platform.concurrent.PlatformTask
 * @author Chuanwise
 */
@SuppressWarnings("all")
public abstract class AbstractPlatformTask
        extends AbstractPluginObject
        implements PlatformTask {
    
    @Getter
    protected volatile Throwable cause;
    
    /**
     * 平台任务监听器
     */
    protected final List<PlatformTaskListener> listeners = new CopyOnWriteArrayList<>();
    
    public AbstractPlatformTask(Platform platform, Plugin plugin) {
        super(platform, plugin);
    }
    
    @Override
    public PlatformTask addListener(PlatformTaskListener listener) {
        Preconditions.namedArgumentNonNull(listener, "listener");
        
        listeners.add(listener);
    
        if (isDone()) {
            listener.listen(this);
        }
        
        return this;
    }
    
    @Override
    public PlatformTask addListeners(PlatformTaskListener... listeners) {
        Preconditions.namedArgumentNonNull(listeners, "listeners");
        
        this.listeners.addAll(Arrays.asList(listeners));
    
        if (isDone()) {
            for (PlatformTaskListener listener : listeners) {
                listener.listen(this);
            }
        }
        
        return this;
    }
    
    @Override
    public PlatformTask addListeners(Iterable<? extends PlatformTaskListener> iterable) {
        Preconditions.namedArgumentNonNull(iterable, "iterable");
    
        iterable.forEach(listener -> {
            listeners.add(listener);
            if (isDone()) {
                listener.listen(this);
            }
        });
    
        return this;
    }
    
    @Override
    public boolean await(long timeout, TimeUnit timeUnit) throws InterruptedException {
        Preconditions.namedArgumentNonNull(timeUnit, "time unit");
        Preconditions.argument(timeout >= 0, "time out must be bigger than or equals to 0!");
        
        return await(timeUnit.toMillis(timeout));
    }
    
    @Override
    public boolean awaitUninterruptibly(long timeout, TimeUnit timeUnit) {
        Preconditions.namedArgumentNonNull(timeUnit, "time unit");
        Preconditions.argument(timeout >= 0, "time out must be bigger than or equals to 0!");
    
        return awaitUninterruptibly(timeUnit.toMillis(timeout));
    }
    
    @Override
    public PlatformTask sync() throws InterruptedException {
        return await();
    }
    
    @Override
    public PlatformTask syncUninterruptibly() {
        return awaitUninterruptibly();
    }
}
