package cn.codethink.xiaoming.platform.concurrent;

import java.util.concurrent.*;

/**
 * 平台 Future
 *
 * @author Chuanwise
 * @param <T> 返回类型
 */
public interface PlatformFuture<T>
        extends PlatformTask, Future<T>, Callable<T> {
    
    /**
     * 阻塞当前线程，直到获取返回值
     *
     * @param timeout 超时时长
     * @return 返回值
     * @throws InterruptedException 中断异常
     * @throws ExecutionException   执行失败异常
     * @throws TimeoutException     超时异常
     */
    T get(long timeout) throws InterruptedException, ExecutionException, TimeoutException;
}
