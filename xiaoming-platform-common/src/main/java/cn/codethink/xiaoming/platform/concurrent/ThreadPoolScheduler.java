package cn.codethink.xiaoming.platform.concurrent;

import cn.codethink.common.api.ExceptionRunnable;
import cn.codethink.common.api.ExceptionSupplier;
import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.concurrent.PeriodBotTask;
import cn.codethink.xiaoming.concurrent.PeriodBotTaskAdapter;
import cn.codethink.xiaoming.concurrent.PeriodThreadBotTask;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * 线程池调度器
 *
 * @author Chuanwise
 */
public class ThreadPoolScheduler
    extends AbstractScheduler {
    
    protected final ScheduledExecutorService threadPool;
    
    @SuppressWarnings("all")
    public ThreadPoolScheduler(Platform platform, int threadCount) {
        super(platform);
    
        Preconditions.argument(threadCount > 0, "thread count must be bigger than 0!");
        threadPool = Executors.newScheduledThreadPool(threadCount);
    }
    
    @Override
    public PlatformTask submit(ExceptionRunnable action, Plugin plugin) {
        Preconditions.namedArgumentNonNull(action, "action");
    
        final SimpleThreadPlatformTask task = new SimpleThreadPlatformTask(platform, plugin, action);
        final Future<?> future = threadPool.submit(task);
        
        return new PlatformTaskAdapter(task) {
            @Override
            public boolean cancel(boolean interrupt) {
                final boolean superCancelled = super.cancel(interrupt);
                final boolean threadPoolCancelled = future.cancel(interrupt);
                
                return superCancelled && threadPoolCancelled;
            }
        };
    }
    
    @Override
    public PlatformTask schedule(ExceptionRunnable action, Plugin plugin, long delay, TimeUnit timeUnit) {
        Preconditions.namedArgumentNonNull(action, "action");
        Preconditions.namedArgumentNonNull(timeUnit, "time unit");
        Preconditions.argument(delay >= 0, "delay must be bigger than or equals to 0!");

        return null;
    }
    
    @Override
    public <T> PlatformFuture<T> submit(Callable<T> action, Plugin plugin) {
        Preconditions.namedArgumentNonNull(action, "action");
        
        final SimpleThreadPlatformFuture<T> task = new SimpleThreadPlatformFuture<>(platform, plugin, action);
        final Future<?> future = threadPool.submit((Runnable) task);
        
        return new PlatformFutureAdapter<T>(task) {
            @Override
            public boolean cancel(boolean interrupt) {
                final boolean superCancelled = super.cancel(interrupt);
                final boolean threadPoolCancelled = future.cancel(interrupt);
    
                return superCancelled && threadPoolCancelled;
            }
        };
    }
    
    @Override
    public <T> PlatformFuture<T> schedule(Callable<T> action, Plugin plugin, long delay, TimeUnit timeUnit) {
        Preconditions.namedArgumentNonNull(action, "action");
        Preconditions.namedArgumentNonNull(timeUnit, "time unit");
        Preconditions.argument(delay >= 0, "delay must be bigger than or equals to 0!");
    
        final SimpleThreadPlatformFuture<T> task = new SimpleThreadPlatformFuture<>(platform, plugin, action);
        final Future<?> future = threadPool.schedule((Runnable) task, delay, timeUnit);
    
        return new PlatformFutureAdapter<T>(task) {
            @Override
            public boolean cancel(boolean interrupt) {
                final boolean superCancelled = super.cancel(interrupt);
                final boolean threadPoolCancelled = future.cancel(interrupt);
            
                return superCancelled && threadPoolCancelled;
            }
        };
    }
    
    @Override
    public List<PlatformTask> cancelTasks(Plugin plugin, boolean interrupt) {
        final BlockingQueue<Runnable> queue = ((ThreadPoolExecutor) threadPool).getQueue();
        
        final List<PlatformTask> cancelledTasks = new ArrayList<>();
        queue.removeIf(task -> {
            final PlatformTask platformTask = (PlatformTask) task;
            if (platformTask.getPlugin() != plugin) {
                return false;
            }
            
            final boolean cancelled = platformTask.cancel(interrupt);
            if (cancelled) {
                cancelledTasks.add(platformTask);
            }
            return cancelled;
        });
        
        return Collections.unmodifiableList(
            cancelledTasks
        );
    }
    
    @Override
    public List<PlatformTask> cancelAllTasks(boolean interrupt) {
        final BlockingQueue<Runnable> queue = ((ThreadPoolExecutor) threadPool).getQueue();
    
        final List<PlatformTask> cancelledTasks = new ArrayList<>();
        queue.removeIf(task -> {
            final PlatformTask platformTask = (PlatformTask) task;
            final boolean cancelled = platformTask.cancel(interrupt);
            if (cancelled) {
                cancelledTasks.add(platformTask);
            }
            return cancelled;
        });
    
        return Collections.unmodifiableList(
            cancelledTasks
        );
    }
    
    @Override
    public PeriodPlatformTask scheduleWithFixedDelay(ExceptionRunnable action, Plugin plugin, long period) {
        Preconditions.namedArgumentNonNull(action, "action");
        Preconditions.argument(period > 0, "period must be bigger than 0!");
        
        final PeriodThreadPlatformTask task = new PeriodThreadPlatformTask(platform, plugin, action);
        final ScheduledFuture<?> future = threadPool.scheduleWithFixedDelay(task, 0, period, TimeUnit.MILLISECONDS);
        
        return new PeriodPlatformTaskAdapter(task) {
            @Override
            public boolean cancel(boolean interrupt) {
                final boolean superCancelled = super.cancel(interrupt);
                final boolean threadPoolCancelled = future.cancel(interrupt);
    
                return superCancelled && threadPoolCancelled;
            }
        };
    }
    
    @Override
    public PeriodPlatformTask scheduleAtFixedRate(ExceptionRunnable action, Plugin plugin, long period) {
        Preconditions.namedArgumentNonNull(action, "action");
        Preconditions.argument(period > 0, "period must be bigger than 0!");
        
        final PeriodThreadPlatformTask task = new PeriodThreadPlatformTask(platform, plugin, action);
        final ScheduledFuture<?> future = threadPool.scheduleAtFixedRate(task, 0, period, TimeUnit.MILLISECONDS);
    
        return new PeriodPlatformTaskAdapter(task) {
            @Override
            public boolean cancel(boolean interrupt) {
                final boolean superCancelled = super.cancel(interrupt);
                final boolean threadPoolCancelled = future.cancel(interrupt);
            
                return superCancelled && threadPoolCancelled;
            }
        };
    }
    
    @Override
    public boolean isShutdown() {
        return threadPool.isShutdown();
    }
    
    @Override
    public boolean isTerminated() {
        return threadPool.isTerminated();
    }
    
    @Override
    public void shutdownGracefully() {
        threadPool.shutdown();
    }
    
    @Override
    public void shutdownImmediately() {
        threadPool.shutdownNow();
    }
    
    @Override
    public boolean awaitTermination(long timeout) throws InterruptedException {
        Preconditions.argument(timeout >= 0, "timeout must be bigger than or equals to 0!");
        
        if (timeout == 0) {
            awaitTermination();
            return true;
        } else {
            return threadPool.awaitTermination(timeout, TimeUnit.MILLISECONDS);
        }
    }
    
    @Override
    @SuppressWarnings("all")
    public void awaitTermination() throws InterruptedException {
        while (!isTerminated()) {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        }
    }
}
