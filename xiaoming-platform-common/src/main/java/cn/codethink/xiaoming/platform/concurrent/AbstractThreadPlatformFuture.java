package cn.codethink.xiaoming.platform.concurrent;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.plugin.Plugin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 线程
 *
 * @author Chuanwise
 */
public abstract class AbstractThreadPlatformFuture<T>
        extends AbstractThreadPlatformTask
        implements PlatformFuture<T> {
    
    protected volatile T value;
    
    public AbstractThreadPlatformFuture(Platform platform, Plugin plugin) {
        super(platform, plugin);
    }
    
    @Override
    protected final void run0() throws Exception {
        value = call();
    }
    
    @Override
    public T get() throws InterruptedException, ExecutionException {
        if (!isDone()) {
            await();
        }
    
        if (isFailed()) {
            throw new ExecutionException(cause);
        }
        
        return value;
    }
    
    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        Preconditions.argument(timeout >= 0, "time out must be bigger than or equals to 0!");
        Preconditions.namedArgumentNonNull(unit, "time unit");
        
        return get(unit.toMillis(timeout));
    }
    
    @Override
    public T get(long timeout) throws InterruptedException, ExecutionException, TimeoutException {
        Preconditions.argument(timeout >= 0, "time out must be bigger than or equals to 0!");
    
        if (!isDone()) {
            if (!await(timeout)) {
                throw new TimeoutException();
            }
        }
    
        if (isFailed()) {
            throw new ExecutionException(cause);
        }
    
        return value;
    }
}
