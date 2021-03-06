package cn.codethink.xiaoming.platform.concurrent;

import cn.codethink.common.api.ExceptionRunnable;
import cn.codethink.common.api.ExceptionSupplier;
import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.concurrent.PeriodBotTask;
import cn.codethink.xiaoming.platform.AbstractPlatformObject;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.plugin.Plugin;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @see cn.codethink.xiaoming.platform.concurrent.Scheduler
 * @author Chuanwise
 */
public abstract class AbstractScheduler
        extends AbstractPlatformObject
        implements Scheduler {
    
    public AbstractScheduler(Platform platform) {
        super(platform);
    }
    
    @Override
    public PlatformTask schedule(ExceptionRunnable action, Plugin plugin, long delay) {
        Preconditions.namedArgumentNonNull(action, "action");
        Preconditions.argument(delay >= 0, "delay must be bigger than or equals to 0!");
        
        return schedule(action, plugin, delay, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public <T> PlatformFuture<T> schedule(Callable<T> action, Plugin plugin, long delay) {
        Preconditions.namedArgumentNonNull(action, "action");
        Preconditions.argument(delay >= 0, "delay must be bigger than or equals to 0!");
        
        return schedule(action, plugin, delay, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public PeriodPlatformTask scheduleWithFixedDelay(ExceptionRunnable action, Plugin plugin, long period, TimeUnit timeUnit) {
        Preconditions.namedArgumentNonNull(action, "action");
        Preconditions.namedArgumentNonNull(timeUnit, "time unit");
        Preconditions.argument(period > 0, "period must be bigger than 0!");
        
        return scheduleWithFixedDelay(action, plugin, timeUnit.toMillis(period));
    }
    
    @Override
    public PeriodPlatformTask scheduleAtFixedRate(ExceptionRunnable action, Plugin plugin, long period, TimeUnit timeUnit) {
        Preconditions.namedArgumentNonNull(action, "action");
        Preconditions.namedArgumentNonNull(timeUnit, "time unit");
        Preconditions.argument(period > 0, "period must be bigger than 0!");
        
        return scheduleAtFixedRate(action, plugin, timeUnit.toMillis(period));
    }
    
    @Override
    public boolean awaitTermination(long timeout, TimeUnit timeUnit) throws InterruptedException {
        Preconditions.namedArgumentNonNull(timeUnit, "time unit");
        Preconditions.argument(timeout >= 0, "timeout must be bigger than or equals to 0!");
        
        return awaitTermination(timeUnit.toMillis(timeout));
    }
    
    @Override
    public void awaitTerminationUninterruptibly() {
        while (!isTerminated()) {
            try {
                awaitTermination();
            } catch (InterruptedException ignored) {
            }
        }
    }
    
    @Override
    public boolean awaitTerminationUninterruptibly(long timeout) {
        Preconditions.argument(timeout >= 0, "timeout must be bigger than or equals to 0!");
        
        if (timeout == 0) {
            awaitTerminationUninterruptibly();
            return true;
        }
        
        final long deadline = System.currentTimeMillis() + timeout;
        while (!isTerminated()) {
            final long remain = deadline - System.currentTimeMillis();
            if (remain > 0) {
                try {
                    awaitTermination(timeout);
                } catch (InterruptedException ignored) {
                }
            } else {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean awaitTerminationUninterruptibly(long timeout, TimeUnit timeUnit) {
        Preconditions.namedArgumentNonNull(timeUnit, "time unit");
        Preconditions.argument(timeout >= 0, "timeout must be bigger than or equals to 0!");
        
        return awaitTerminationUninterruptibly(timeUnit.toMillis(timeout));
    }
    
    @Override
    public void shutdownSync() throws InterruptedException {
        shutdownGracefully();
        awaitTermination();
    }
    
    @Override
    public boolean shutdownSync(long timeout) throws InterruptedException {
        Preconditions.argument(timeout >= 0, "timeout must be bigger than or equals to 0!");
        
        shutdownGracefully();
        return awaitTermination(timeout);
    }
    
    @Override
    public boolean shutdownSync(long timeout, TimeUnit timeUnit) throws InterruptedException {
        Preconditions.namedArgumentNonNull(timeUnit, "time unit");
        Preconditions.argument(timeout >= 0, "timeout must be bigger than or equals to 0!");
        
        shutdownGracefully();
        return awaitTermination(timeout, timeUnit);
    }
    
    @Override
    public boolean shutdownUninterruptibly(long timeout) {
        Preconditions.argument(timeout >= 0, "timeout must be bigger than or equals to 0!");
        
        shutdownGracefully();
        return awaitTerminationUninterruptibly(timeout);
    }
    
    @Override
    public boolean shutdownUninterruptibly(long timeout, TimeUnit timeUnit) {
        Preconditions.namedArgumentNonNull(timeUnit, "time unit");
        Preconditions.argument(timeout >= 0, "timeout must be bigger than or equals to 0!");
        
        shutdownGracefully();
        return awaitTerminationUninterruptibly(timeout, timeUnit);
    }
    
    @Override
    public void shutdownUninterruptibly() {
        shutdownGracefully();
        awaitTerminationUninterruptibly();
    }
    
}
