package cn.codethink.xiaoming.platform.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("all")
public class PlatformFutureAdapter<T>
        extends PlatformTaskAdapter
        implements PlatformFuture<T> {
    
    public PlatformFutureAdapter(PlatformFuture<T> task) {
        super(task);
    }
    
    @Override
    public T get(long timeout) throws InterruptedException, ExecutionException, TimeoutException {
        return ((PlatformFuture<T>) task).get(timeout);
    }
    
    @Override
    public T call() throws Exception {
        return ((PlatformFuture<T>) task).call();
    }
    
    @Override
    public T get() throws InterruptedException, ExecutionException {
        return ((PlatformFuture<T>) task).get();
    }
    
    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return ((PlatformFuture<T>) task).get(timeout, unit);
    }
}
