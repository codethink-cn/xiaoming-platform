package cn.codethink.xiaoming.platform.concurrent;

import cn.codethink.common.api.ExceptionSupplier;
import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.plugin.Plugin;
import lombok.Data;

import java.util.concurrent.Callable;

/**
 * 用 ExceptionRunnable 实现的平台 Future
 *
 * @author Chuanwise
 */
@Data
@SuppressWarnings("all")
public class SimpleThreadPlatformFuture<T>
        extends AbstractThreadPlatformFuture<T> {
    
    private final Callable<T> action;
    
    public SimpleThreadPlatformFuture(Platform platform, Plugin plugin, Callable<T> action) {
        super(platform, plugin);
    
        Preconditions.namedArgumentNonNull(action, "action");
        
        this.action = action;
    }
    
    @Override
    public T call() throws Exception {
        return action.call();
    }
}
